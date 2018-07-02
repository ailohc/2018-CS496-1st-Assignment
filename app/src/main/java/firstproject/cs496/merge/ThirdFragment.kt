package firstproject.cs496.merge

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



class ThirdFragment : Fragment() {

    companion object {
        fun newInstance(): ThirdFragment {
            val fragment = ThirdFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_third, container, false);
        var context = rootView.getContext();
        var intent = Intent(context, ThirdActivity::class.java)
        startActivity(intent)

        return rootView;
    }
}
