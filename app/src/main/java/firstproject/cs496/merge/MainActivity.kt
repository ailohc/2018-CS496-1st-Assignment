package firstproject.cs496.merge

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.TabLayout
import android.support.v4.content.PermissionChecker
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.net.Uri
import android.net.Uri.withAppendedPath
import java.io.BufferedInputStream


class MainActivity : AppCompatActivity() {
    private var mSectionsPagerAdapter: PageAdapter?=null
    private var mViewPager: ViewPager? = null

    companion object {
        public var contactsList: ArrayList<PhoneNumber>? = null
    }

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
        var contactImage: String
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
                    contactImage = buf.toString()
                }
                else {
                    contactImage = default_photo.toString()
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
            //toast("No contacts available!")
        }
        cursor.close()
        return phoneNumberList
    }
}

