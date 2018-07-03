package firstproject.cs496.merge

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import firstproject.cs496.merge.R
import firstproject.cs496.merge.PhoneNumber
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.sdk25.coroutines.onClick

class PhoneNumberAdapter (val contactList: ArrayList<PhoneNumber>) : RecyclerView.Adapter<PhoneNumberAdapter.ContactViewHolder>() {

    private lateinit var listener: OnItemSelectedListener


    fun setClickListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.phonenumber_item, parent, false)

        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        holder.name.text = contactList[position].name
        holder.phone.text = contactList[position].phone


        Glide.with(holder.container).load(contactList[position].image).into(holder.profilePic)


        holder.container.onClick {
            listener.onItemSelected(contactList[position])
        }
    }

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val container = itemView.findViewById<CardView>(R.id.container)
        val name = itemView.findViewById<TextView>(R.id.tv_name)
        val phone = itemView.findViewById<TextView>(R.id.tv_phone)
        val profilePic = itemView.findViewById<CircleImageView>(R.id.iv_profile)

    }

    interface OnItemSelectedListener {

        fun onItemSelected(selectedContact: PhoneNumber)
    }

}