package firstproject.cs496.merge

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker.checkSelfPermission
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import firstproject.cs496.merge.MainActivity.Companion.contactsList
import firstproject.cs496.merge.R
import firstproject.cs496.merge.PhoneNumber
import firstproject.cs496.merge.PhoneNumberAdapter
import kotlinx.android.synthetic.main.fragment_first.*


class FirstFragment() : Fragment() {


    companion object {
        fun newInstance(): FirstFragment {
            val fragment = FirstFragment()
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater!!.inflate(R.layout.fragment_first, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.phonenumber_recycler_view) as RecyclerView
        recyclerView.adapter = PhoneNumberAdapter(contactsList!!)
        val formanage = LinearLayoutManager(activity)
        recyclerView.layoutManager = formanage
        recyclerView.setHasFixedSize(false)
        return rootView
    }

}




