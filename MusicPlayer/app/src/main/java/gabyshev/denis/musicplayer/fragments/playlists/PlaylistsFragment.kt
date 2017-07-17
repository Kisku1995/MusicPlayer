package gabyshev.denis.musicplayer.fragments.playlists

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R

/**
 * Created by 1 on 17.07.2017.
 */
class PlaylistsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_playlists, container, false)
        return view
    }
}