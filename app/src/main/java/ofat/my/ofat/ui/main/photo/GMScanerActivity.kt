package ofat.my.ofat.ui.main.photo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.BarcodeDetector
import ofat.my.ofat.R
import ofat.my.ofat.ui.main.FoundListViewModel
import ofat.my.ofat.ui.main.goods.GoodViewModel
import timber.log.Timber
import java.io.IOException
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.Detector



class GMScanerActivity : AppCompatActivity() {

    private var detector: BarcodeDetector? = null

    private var progress: ProgressBar? = null

    private var surfaceView : SurfaceView? = null

    lateinit var foundViewModel: FoundListViewModel

    lateinit var goodViewModel: GoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmscaner)
        surfaceView = findViewById(R.id.surfaceView)
        foundViewModel = ViewModelProviders.of(this).get(FoundListViewModel::class.java)
        goodViewModel = ViewModelProviders.of(this).get(GoodViewModel::class.java)
        setupBarcodeDetector()
//        dispatchTakePictureIntent()
    }
/*

    private fun dispatchTakePictureIntent() {
        val mCameraSource = CameraSource.Builder(applicationContext, detector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(640, 480)
            .setRequestedFps(20.0f)
            .setAutoFocusEnabled(true)
            .build()
        surfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                return
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mCameraSource.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                try {
                    mCameraSource.start(surfaceView?.holder)
                }catch (e: IOException) {

                }
            }

        })
    }
*/

    private fun setupBarcodeDetector() {
        detector = BarcodeDetector.Builder(this)
            .build()
        if (detector != null && !detector!!.isOperational) {
            Toast.makeText(this, "Ошибка детектора штрих кодов.", Toast.LENGTH_SHORT).show()
            Timber.e("Ошибка детектора штрих кодов.")
            this.finish()
        }
        detector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                return
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems

                if (barcodes.size() != 0) {
                    println(barcodes.size())
                }
            }
        })
    }
}
