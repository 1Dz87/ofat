package ofat.my.ofat.ui.main.transaction

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import ofat.my.ofat.MainActivity
import ofat.my.ofat.OfatApplication

import ofat.my.ofat.R
import ofat.my.ofat.ui.main.FoundListViewModel
import ofat.my.ofat.ui.main.goods.GoodViewModel
import timber.log.Timber
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ofat.my.ofat.Util.CollectionUtils
import ofat.my.ofat.model.Good
import ofat.my.ofat.persistence.OfatDatabase


class FastTxScFragment : Fragment() {

    private var detector: BarcodeDetector? = null

    private var surfaceView: SurfaceView? = null

    private var mCameraSource: CameraSource? = null

    private var currentCode: String? = null

    lateinit var foundViewModel: FoundListViewModel

    lateinit var goodViewModel: GoodViewModel

    lateinit var toCartBt: FloatingActionButton

    private var textHandler: Handler? = null

    private var good: Good? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fast_tx_sc, container, false)
        textHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(message: Message) {
                Toast.makeText(this@FastTxScFragment.context, message.obj as String, Toast.LENGTH_SHORT).show()
            }
        }
        toCartBt = view.findViewById(R.id.btToCart)
        toCartBt.setOnClickListener {
            if (CollectionUtils.mapIsEmpty(goodViewModel.getBuyCart().value)) {
                textHandler?.obtainMessage(0, "Корзина пуста.")
            } else {
                view.findNavController().navigate(R.id.fragment_cart)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).setCurrentTitle(R.string.scanner)
        surfaceView = view?.findViewById(R.id.surfaceView)
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
        good = OfatDatabase.getInstance(this@FastTxScFragment.context!!).goodDao().getByBarcode(rawValue)
        if (good != null) {
            val bundle = Bundle()
            bundle.putParcelable("good", good)
            view?.findNavController()?.navigate(R.id.fastTxCartQuantityFragment, bundle)
        } else {
            textHandler?.obtainMessage(0, "Товар не найден.")
        }
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
                    currentCode = barcodes.valueAt(0)!!.rawValue
                    barcodeRequest(currentCode!!)
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
