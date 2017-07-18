package gabyshev.denis.musicplayer.fragments.playlists

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.TracksHelper
import org.jetbrains.anko.find

/**
 * Created by 1 on 17.07.2017.
 */
class PlaylistsFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PlaylistsAdapter(context, TracksHelper.instance().scanForPlaylists(context))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_recycler_view, container, false)
        recyclerView = view.find(R.id.recyclerView)
        return view
    }
}