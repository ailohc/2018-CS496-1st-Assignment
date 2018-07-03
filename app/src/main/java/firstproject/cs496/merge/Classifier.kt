package firstproject.cs496.merge

import android.graphics.Bitmap

interface Classifier {

    class Recognition(

            val id: String?,
            val title: String?,
            val confidence: Float?) {

        override fun toString(): String {
            var resultString = ""
            if (id != null) {
                resultString += "[$id] "
            }

            if (title != null) {
                resultString += "$title "
            }

            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f)
            }

            return resultString.trim { it <= ' ' }
        }
    }


    fun recognizeImage(bitmap: Bitmap): List<Recognition>

    fun close()
}
