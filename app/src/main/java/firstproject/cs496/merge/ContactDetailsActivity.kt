package firstproject.cs496.merge

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import firstproject.cs496.merge.R
import kotlinx.android.synthetic.main.phonenumber_details.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class ContactDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phonenumber_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tv_name.text = intent.extras.getString("name")
        tv_phone.text = intent.extras.getString("phone")

        val imagebtmp = intent.extras.getString("imageUrl")
        val imageresult = StringToBitMap(imagebtmp)
        iv_profile.setImageBitmap(imageresult)
        tv_name.onClick {
            toast(tv_name.text)
        }

        tv_phone.onClick {
            toast(tv_phone.text)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return true
    }

    fun StringToBitMap(encodedString: String) : Bitmap? {
        try{
            val encodeByte = Base64.decode(encodedString,Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            return bitmap
        }catch(e:Exception){
            e.message
            return null
        }
    }
}
