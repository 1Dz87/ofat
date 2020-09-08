package ofat.my.ofat.ui.main.photo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.vision.barcode.BarcodeDetector
import ofat.my.ofat.OfatApplication
import ofat.my.ofat.R
import ofat.my.ofat.api.response.GetGoodShortViewResponse
import ofat.my.ofat.model.ShortView
import ofat.my.ofat.ui.main.FoundListViewModel
import ofat.my.ofat.ui.main.goods.GoodViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import ofat.my.ofat.MainActivity
import ofat.my.ofat.Util.OfatConstants
import ofat.my.ofat.Util.WebUtil

class DefaultScannerFragment : Fragment() {

    private var detector: BarcodeDetector? = null

    private var progress: ProgressBar? = null

    private var surfaceView: SurfaceView? = null

    private var scanner_TV: TextView? = null

    private var mCameraSource: CameraSource? = null

    private var currentCode: String? = null

    lateinit var foundViewModel: FoundListViewModel

    lateinit var goodViewModel: GoodViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.default_scanner_fragment, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.scanner)
        progress = view?.findViewById(R.id.progressBarBC)
        surfaceView = view?.findViewById(R.id.surfaceView)
        scanner_TV = view?.findViewById(R.id.scanner_TV)
        foundViewModel = ViewModelProviders.of(activity!!).get(FoundListViewModel::class.java)
        goodViewModel = ViewModelProviders.of(activity!!).get(GoodViewModel::class.java)
        setupBarcodeDetector()
        dispatchTakePictureIntent()
    }

    @SuppressLint("MissingPermission")
    private fun dispatchTakePictureIntent() {
        mCameraSource = CameraSource.Builder(context, detector)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(1600, 900)
            .setRequestedFps(15.0f)
            .setAutoFocusEnabled(true)
            .build()
        surfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                return
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mCameraSource?.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                mCameraSource?.start(surfaceView?.holder)
            }

        })
    }

    private fun barcodeRequest(rawValue: String) {
        val call = OfatApplication.goodApi?.getGoodShortView(rawValue)
        call?.enqueue(object : Callback<GetGoodShortViewResponse> {
            @SuppressLint("ThrowableNotAtBeginning", "TimberExceptionLogging")
            override fun onFailure(call: Call<GetGoodShortViewResponse>, t: Throwable) {
                Toast.makeText(context, "Ошибка сервера.", Toast.LENGTH_SHORT).show()
                Timber.e(t, t.message, t.cause)
            }

            override fun onResponse(
                call: Call<GetGoodShortViewResponse>,
                response: Response<GetGoodShortViewResponse>
            ) {
                view?.findNavController()?.popBackStack()
                if (response.body()?.success != null) {
                    foundViewModel.foundList.value = response.body()?.success as MutableCollection<ShortView>
                    view?.findNavController()?.navigate(R.id.foundListFragment)
                } else {
                    Toast.makeText(context, WebUtil.checkUnauthCode(response, "Товар не найден.") { val bundle = Bundle()
                        bundle.putString("barcode", rawValue)
                        view?.findNavController()?.navigate(R.id.goodsFragment, bundle) }, Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun showYesNoDialog(title: String, message: String, yes: String = "Да", cancel: String = "Нет") {
        val dialog = this.let { AlertDialog.Builder(it.context) }
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(yes) { dialog1, _ ->
            view?.findNavController()?.navigate(R.id.goodsFragment)
            dialog1.dismiss()
        }
        dialog.setNegativeButton(cancel) { dialog1, _ ->
            dialog1.dismiss()
        }
        dialog.show()
    }

    private fun setupBarcodeDetector() {
        detector = BarcodeDetector.Builder(this.context)
            .build()
        if (detector != null && !detector!!.isOperational) {
            Toast.makeText(this.context, "Ошибка детектора штрих кодов.", Toast.LENGTH_SHORT).show()
            Timber.e("Ошибка детектора штрих кодов.")
            view?.findNavController()?.navigateUp()
        }
        detector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                return
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems

                if (barcodes?.size() != 0 && barcodes?.valueAt(0)!!.rawValue != currentCode) {
//                    thread {
                        currentCode = barcodes.valueAt(0)!!.rawValue
                        barcodeRequest(currentCode!!)
//                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mCameraSource?.release()
        detector?.release()
    }

    override fun onResume() {
        super.onResume()
        setupBarcodeDetector()
        dispatchTakePictureIntent()
    }
}