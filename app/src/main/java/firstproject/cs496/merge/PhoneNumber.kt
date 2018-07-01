package firstproject.cs496.merge

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import android.provider.ContactsContract


class PhoneNumber(
        val name: String,
        val phonenumber: String) {
/*
    companion object {

        fun getContactsFromFile(filename: String, context: Context): ArrayList<PhoneNumber> {
            val phoneNumberList = ArrayList<PhoneNumber>()

            try {
                // Load data
                val jsonString = loadJsonFromAsset("phone_number.json", context)
                val json = JSONObject(jsonString)
                val phnumbers = json.getJSONArray("phnumbers")

                (0 until phnumbers.length()).mapTo(phoneNumberList) {
                    PhoneNumber(phnumbers.getJSONObject(it).getString("name"),
                            phnumbers.getJSONObject(it).getString("phonenumber"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return phoneNumberList
        }

        private fun loadJsonFromAsset(filename: String, context: Context): String? {
            var json: String? = null

            try {
                val inputStream = context.assets.open(filename)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: java.io.IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }
    }*/

}
