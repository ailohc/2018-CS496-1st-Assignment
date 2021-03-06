package firstproject.cs496.merge

import android.content.ContentResolver
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.View
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import org.jetbrains.anko.toast
import java.io.BufferedInputStream


class MainActivity : AppCompatActivity() {
    private var mSectionsPagerAdapter: PageAdapter?=null
    private var mViewPager: ViewPager? = null

    companion object {
        var contactsList: ArrayList<PhoneNumber>? = null
    } //used as companion object

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSectionsPagerAdapter = PageAdapter(supportFragmentManager)
        mViewPager = findViewById<ViewPager?>(R.id.container)
        mViewPager!!.adapter = mSectionsPagerAdapter
        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)
    }

    override fun onStart() {
        super.onStart()
        contactsList = getContacts()
    }

    override fun onResume() {
        super.onResume()
        contactsList = getContacts()
    }

    private fun getContacts() : ArrayList<PhoneNumber> {
        val phoneNumberList = ArrayList<PhoneNumber>()
        val resolver: ContentResolver = contentResolver;
        val default_photo = BitmapFactory.decodeResource(this@MainActivity.getApplicationContext().getResources(), R.drawable.profile_pic)
        var contactImage: Bitmap
        val contentResolver = this@MainActivity.getContentResolver()
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                null)

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id)
                val photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(this@MainActivity.getContentResolver(), my_contact_Uri)
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
                if (photo_stream != null) {
                    val buf = BufferedInputStream(photo_stream)
                    val btmp = BitmapFactory.decodeStream(buf)
                    contactImage = btmp
                }
                else {
                    contactImage = default_photo
                }
                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                    if(cursorPhone.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(
                                    cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            phoneNumberList.add(PhoneNumber(name, phoneNumValue, contactImage))
                        }
                    }
                    cursorPhone.close()
                }
            }
        } else {
            toast("No contacts available!")
        }
        cursor.close()
        return phoneNumberList
    }
}

