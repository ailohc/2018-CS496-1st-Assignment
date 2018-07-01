package firstproject.cs496.merge
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import firstproject.cs496.merge.R
import firstproject.cs496.merge.PhoneNumber
import firstproject.cs496.merge.R.layout.phonenumber_item


class PhoneNumberAdapter(val contactList: ArrayList<PhoneNumber>) : RecyclerView.Adapter<PhoneNumberAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.phonenumber_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(contactList[position])
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val nameText = itemView?.findViewById<TextView>(R.id.phonenumber_list_name)
        val phnumberText = itemView?.findViewById<TextView>(R.id.phonenumber_list_phnumber)

        fun bind (phonenumber: PhoneNumber) {
            nameText?.text = phonenumber.name
            phnumberText?.text = phonenumber.phonenumber
        }
    }
}