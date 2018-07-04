package firstproject.cs496.merge

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.widget.Button
import kotlinx.android.synthetic.main.phonenumber_details.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import org.jetbrains.anko.makeCall
import org.jetbrains.anko.sendSMS

class ContactDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phonenumber_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tv_name.text = intent.extras.getString("name")
        tv_phone.text = intent.extras.getString("phone")
        var phonecallnumber = tv_phone.text
        phonecallnumber = phonecallnumber.toString()
        tv_name.onClick {
            toast(tv_name.text)
        }

        tv_phone.onClick {
            toast(tv_phone.text)
        }

        val callButton = findViewById<Button>(R.id.button1)
        callButton.setOnClickListener {
            makeCall(phonecallnumber)
        }

        val messageButton = findViewById<Button>(R.id.button2)
        messageButton.setOnClickListener {
            sendSMS(phonecallnumber, "")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return true
    }

}
