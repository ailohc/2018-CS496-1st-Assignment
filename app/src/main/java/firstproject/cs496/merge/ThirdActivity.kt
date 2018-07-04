package firstproject.cs496.merge

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.wonderkiln.camerakit.*
import kotlinx.android.synthetic.main.activity_third.*
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.Executors
import android.os.Environment.getExternalStorageDirectory
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Environment
import android.util.Log
import android.widget.RelativeLayout
import java.io.*
import java.nio.file.Files.exists
import android.content.Intent
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*
import java.nio.file.Files.exists
import android.widget.Toast








class ThirdActivity : AppCompatActivity() {

    private var classifier: Classifier? = null
    private var container: RelativeLayout? = null
    private val executor = Executors.newSingleThreadExecutor()
    private var textViewResult: TextView? = null
    private var btnDetectObject: Button? = null
    private var btnToggleCamera: Button? = null
    private var btnCaptureImage: Button? = null
    private var cameraView: CameraView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        cameraView = findViewById(R.id.cameraView)
        textViewResult = findViewById(R.id.textViewResult)
        textViewResult!!.movementMethod = ScrollingMovementMethod()

        btnToggleCamera = findViewById(R.id.btnToggleCamera)
        btnDetectObject = findViewById(R.id.btnDetectObject)
        btnCaptureImage = findViewById(R.id.btnCaptureImage)

        container = findViewById(R.id.activity_third)
        var width_container = container!!.width
        var height_container = container!!.height

        container!!.setDrawingCacheEnabled(true)
        container!!.buildDrawingCache(true)




        cameraView!!.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {

            }

            override fun onError(cameraKitError: CameraKitError) {

            }

            override fun onImage(cameraKitImage: CameraKitImage) {

                var bitmap = cameraKitImage.bitmap

                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)

                val results = classifier!!.recognizeImage(bitmap)
                var resulttext = results.toString()
                val separate = resulttext.split("[","]","(",")","%")
                val toptext = "The Object might be" + separate[3] + "with " + separate[4] +" percentage"
                textViewResult!!.text = toptext
            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {

            }
        })

        btnToggleCamera!!.setOnClickListener { cameraView!!.toggleFacing() }

        btnDetectObject!!.setOnClickListener { cameraView!!.captureImage() }

        btnCaptureImage!!.setOnClickListener {
            /* val CAPTURE_PATH = "/CAPTURE_TEST"
            val root = this.getWindow().getDecorView().getRootView()
            root.setDrawingCacheEnabled(true)
            root.buildDrawingCache()
            val screenshot = root.getDrawingCache()
            val location = IntArray(2)
            root.getLocationInWindow(location)
            val bmp = Bitmap.createBitmap(screenshot, location[0], location[1], root.width, root.height, null, false)
            val strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + CAPTURE_PATH
            val folder = File(strFolderPath)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png"
            val fileCacheItem = File(strFilePath)
            var out: OutputStream? = null
            try {
                fileCacheItem.createNewFile()
                out = FileOutputStream(fileCacheItem)
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                Log.d("##################", "saved")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                try {
                    out?.close();
                }
                catch (e : IOException) {
                    e.printStackTrace();
                }
            }*/
            /* val CAPTURE_PATH = "/CAPTURE_TEST"
            container!!.buildDrawingCache()
            val captureView = container!!.getDrawingCache()
            var fos: FileOutputStream
            val strFolderPath = Environment.getExternalStorageDirectory().absolutePath + CAPTURE_PATH
            val folder = File(strFolderPath)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val strFilePath = strFolderPath + "/" + System.currentTimeMillis() + ".png"
            val fileCacheItem = File(strFilePath)
            try {
                fos = FileOutputStream(fileCacheItem);
                captureView.compress(Bitmap.CompressFormat.PNG, 100, fos)
            } catch (e: FileNotFoundException) {
                e.printStackTrace();
            }
        }*/
            val folder = "Test_Directory"
            try {
                val formatter = SimpleDateFormat("yyyyMMddHHmmss")
                val currentTime_1 = Date()
                val dateString = formatter.format(currentTime_1)
                val sdCardPath = Environment.getExternalStorageDirectory()
                val dirs = File(Environment.getExternalStorageDirectory(), folder)
                if (!dirs.exists()) {
                    dirs.mkdirs()
                    Log.d("CAMERA_TEST", "Directory Created")
                }
                container!!.buildDrawingCache()
                val captureView = container!!.getDrawingCache()
                val fos: FileOutputStream
                val save: String
                try {
                    save = sdCardPath.path + "/" + folder + "/" + dateString + ".jpg"
                    fos = FileOutputStream(save)
                    captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos) // 캡쳐
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())))
                    Log.d("********************", "*****************")
                } catch (e: FileNotFoundException){e.printStackTrace()}
                Toast.makeText(applicationContext, "$dateString.jpg 저장", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("Screen*************", "" + e.toString())
            }
        }
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



    companion object {

        private val MODEL_PATH = "mobilenet_quant_v1_224.tflite"
        private val LABEL_PATH = "labels.txt"
        private val INPUT_SIZE = 224
    }
}
