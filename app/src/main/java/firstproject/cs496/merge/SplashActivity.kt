package firstproject.cs496.merge

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.Window
import android.os.Handler
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 5000 //3 seconds

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    internal val mRunnable: Runnable = Runnable {

        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                    SplashActivity.PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }

        if (checkSelfPermission(Manifest.permission.CALL_PHONE) !== PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                            Manifest.permission.CALL_PHONE)) {
            }

            val MY_PERMISSIONS_REQUEST_PHONE_CALL = 10
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }

        if (checkSelfPermission(Manifest.permission.SEND_SMS) !== PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                            Manifest.permission.SEND_SMS)) {
            }

            val MY_PERMISSIONS_REQUEST_PHONE_CALL = 10
            requestPermissions(arrayOf(Manifest.permission.SEND_SMS),
                    MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }


        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }

            val MY_PERMISSIONS_REQUEST_PHONE_CALL = 10
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_PHONE_CALL)
        }
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        // Start home activity
    }
}