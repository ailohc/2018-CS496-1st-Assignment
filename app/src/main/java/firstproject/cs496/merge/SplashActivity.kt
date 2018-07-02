package firstproject.cs496.merge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.Window
import android.os.Handler
import android.content.Intent
import android.content.Context

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start home activity
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))

        // close splash activity
        finish()
    }
}