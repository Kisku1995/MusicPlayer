package gabyshev.denis.musicplayer.fragments.tracks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import org.jetbrains.anko.find

/**
 * Created by Borya on 15.07.2017.
 */
class TracksFragment : Fragment() {
    private val TAG = "TracksFragment"
    private lateinit var mRecyclerView: RecyclerView

    companion object {
        private var instance: TracksFragment? = null

        fun instance(): TracksFragment? {
            if(instance == null) {
                instance = TracksFragment()
            }
            return instance!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val array = TracksHelper.instance().scanMP3Files(context)

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = TracksAdapter(context, array)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_tracks, container, false)

        mRecyclerView = view.find(R.id.recyclerView)

        return view
    }
}