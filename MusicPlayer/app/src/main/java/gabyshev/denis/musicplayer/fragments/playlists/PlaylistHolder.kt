package gabyshev.denis.musicplayer.fragments.playlists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.Playlist
import org.jetbrains.anko.find

/**
 * Created by 1 on 18.07.2017.
 */
class PlaylistHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.find(R.id.playlist)

    fun setHolder(context: Context, playlist: Playlist, position: Int) {
        name.text = playlist.name
        TracksHelper.instance().setBackground(context, view, position)
    }
}