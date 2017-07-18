package gabyshev.denis.musicplayer.fragments.select

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import org.jetbrains.anko.find

/**
 * Created by Borya on 18.07.2017.
 */
class SelectFragment: Fragment() {
    private lateinit var selectCount: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_select, container, false)

        selectCount = view.find(R.id.selectCount)

        return view
    }

    fun selectCount(text: String) {
        selectCount.text = text
    }
}