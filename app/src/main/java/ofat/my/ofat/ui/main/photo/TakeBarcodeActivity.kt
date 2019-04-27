package ofat.my.ofat.ui.main.photo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.MediaStore
import android.widget.ProgressBar

import com.camerakit.CameraKitView
import ofat.my.ofat.R
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.vision.barcode.BarcodeDetector
import androidx.core.util.forEach
import com.google.android.gms.vision.Frame
import ofat.my.ofat.OfatApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import com.camerakit.CameraKit
import ofat.my.ofat.Util.UtilUI
import ofat.my.ofat.api.response.GetGoodShortViewResponse
import ofat.my.ofat.model.ShortView

class TakeBarcodeActivity : AppCompatActivity() {

    private var cameraKitView: CameraKitView? = null

    private var detector: BarcodeDetector? = null

    private var progress: ProgressBar? = null

    private var handler: Handler? = null

    private var inProcess: Boolean = false

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.take_barcode_activity)
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                makeText(message.obj as String)
                UtilUI.showProgress(progress!!)
            }
        }
        progress = findViewById(R.id.progressBarBC)
        cameraKitView = findViewById(R.id.camera)
        supportActionBar?.setTitle(R.string.scanner)
        setupBarcodeDetector()
        //setupCamera()
        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, 2)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
        }
    }

    private fun setupBarcodeDetector() {
        detector = BarcodeDetector.Builder(this)
            .build()
        if (detector != null && !detector!!.isOperational) {
            Toast.makeText(this, "Ошибка детектора штрих кодов.", Toast.LENGTH_SHORT).show()
            Timber.e("Ошибка детектора штрих кодов.")
            this@TakeBarcodeActivity.finish()
            onBackPressed()
        }
    }

    fun makeText(text: String) {
        Toast.makeText(this@TakeBarcodeActivity, text, Toast.LENGTH_SHORT).show()
    }

    private fun setupCamera() {
        cameraKitView = findViewById(R.id.camera)
        cameraKitView?.sensorPreset = CameraKit.SENSOR_PRESET_BARCODE

        cameraKitView?.gestureListener = object : CameraKitView.GestureListener {
            override fun onDoubleTap(p0: CameraKitView?, p1: Float, p2: Float) {
                return
            }

            override fun onPinch(p0: CameraKitView?, p1: Float, p2: Float, p3: Float) {
                return
            }

            override fun onLongTap(p0: CameraKitView?, p1: Float, p2: Float) {
                return
            }

            override fun onTap(p0: CameraKitView?, p1: Float, p2: Float) {
                if (!inProcess) {
                    inProcess = true
                    UtilUI.showProgress(progress!!)
                    cameraKitView?.captureImage { _, capturedImage ->
                        val bitmap = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.size)
                        val frame = Frame.Builder().setBitmap(bitmap).build()

                            val barcodes = detector?.detect(frame)
                            bitmap.recycle()
                            if (barcodes == null || barcodes.size() < 1) {
                                val message = handler?.obtainMessage(0, "Штрих код не найден")
                                inProcess = false
                                message?.sendToTarget()
                            } else {
                                barcodes.forEach { _, value ->
                                    barcodeRequest(value.rawValue)
                                }
                            }
                        }
                }
            }
        }
    }

    private fun barcodeRequest(rawValue: String) {
        val call = OfatApplication.goodApi?.getGoodShortView(rawValue)
        call?.enqueue(object : Callback<GetGoodShortViewResponse> {
            @SuppressLint("ThrowableNotAtBeginning", "TimberExceptionLogging")
            override fun onFailure(call: Call<GetGoodShortViewResponse>, t: Throwable) {
                val message = handler?.obtainMessage(0, "Ошибка сервера.")
                message?.sendToTarget()
                Timber.e(t, t.message, t.cause)
            }

            override fun onResponse(
                call: Call<GetGoodShortViewResponse>,
                response: Response<GetGoodShortViewResponse>
            ) {
                if (response.body()?.success != null) {
                    OfatApplication.sharedStore["goods"] = response.body()?.success as MutableCollection<ShortView>
                    UtilUI.showProgress(progress!!)
                    this@TakeBarcodeActivity.finish()
                } else {
                    OfatApplication.sharedStore["barcode"] = rawValue
                    UtilUI.showProgress(progress!!)
                    showYesNoDialog(
                        "Результат",
                        "Товар не найден. Желаете добавить его в базу данных?"
                    )
                }
            }
        })
    }

    private fun showYesNoDialog(title: String, message: String, yes: String = "Да", cancel: String = "Нет") {
        val dialog = this.let { AlertDialog.Builder(it) }
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(yes) { dialog1, _ ->
            dialog1.dismiss()
            this@TakeBarcodeActivity.finish()
        }
        dialog.setNegativeButton(cancel) { dialog1, _ ->
            inProcess = false
            dialog1.dismiss() }
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        cameraKitView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        cameraKitView?.onResume()
    }

    override fun onPause() {
        cameraKitView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        cameraKitView?.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

