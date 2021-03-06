package firstproject.cs496.merge

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.wonderkiln.camerakit.*
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.Executors
import android.widget.RelativeLayout
import android.os.*


class ThirdActivity : AppCompatActivity() {

    private var classifier: Classifier? = null
    private var container: RelativeLayout? = null
    private val executor = Executors.newSingleThreadExecutor()
    private var textViewResult: TextView? = null
    private var btnDetectObject: Button? = null
    private var btnToggleCamera: Button? = null
    private var btnCaptureImage: Button? = null
    private var cameraView: CameraView? = null

    companion object {
        private val MODEL_PATH = "mobilenet_quant_v1_224.tflite"
        private val LABEL_PATH = "labels.txt"
        private val INPUT_SIZE = 224
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        cameraView = findViewById(R.id.cameraView)
        textViewResult = findViewById(R.id.textViewResult)
        textViewResult!!.movementMethod = ScrollingMovementMethod()
        btnToggleCamera = findViewById(R.id.btnToggleCamera)
        btnDetectObject = findViewById(R.id.btnDetectObject)

        cameraView!!.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {

            }

            override fun onError(cameraKitError: CameraKitError) {

            }

            override fun onImage(cameraKitImage: CameraKitImage) {
                val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    v.vibrate(500)
                }
                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)
                val results = classifier!!.recognizeImage(bitmap)
                var resulttext = results.toString()
                val separate = resulttext.split("[","]","(",")","%")
                val toptext =  separate[3] + " , " + separate[4]
                textViewResult!!.text = toptext
            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {

            }
        })
        btnToggleCamera!!.setOnClickListener { cameraView!!.toggleFacing() }
        btnDetectObject!!.setOnClickListener { cameraView!!.captureImage() }
        initTensorFlowAndLoadModel()
    }

    override fun onResume() {
        super.onResume()
        cameraView!!.start()
    }

    override fun onPause() {
        cameraView!!.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.execute { classifier!!.close() }
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = TensorFlowImageClassifier.create(
                        assets,
                        MODEL_PATH,
                        LABEL_PATH,
                        INPUT_SIZE)
                makeButtonVisible()
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    private fun makeButtonVisible() {
        runOnUiThread { btnDetectObject!!.visibility = View.VISIBLE }
    }


}
