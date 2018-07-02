package firstproject.cs496.merge

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_third.button1


class ThirdFragment : Fragment() {

    companion object {
        fun newInstance(): ThirdFragment {
            val fragment = ThirdFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_third, container, false)
        val button1 = rootView.findViewById<Button>(R.id.button1)

        button1.setOnClickListener {
            var intent = Intent(context, ThirdActivity::class.java)
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return rootView
    }

}
