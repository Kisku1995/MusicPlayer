package gabyshev.denis.musicplayer.fragments.playlists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.data.Playlist

/**
 * Created by 1 on 18.07.2017.
 */
class PlaylistsAdapter(private val context: Context, private val playlistsArray: ArrayList<Playlist>): RecyclerView.Adapter<PlaylistHolder>() {
    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.setHolder(context, playlistsArray[position], position)
    }

    override fun getItemCount(): Int = playlistsArray.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaylistHolder {
        return PlaylistHolder(LayoutInflater.from(context).inflate(R.layout.fragment_playlist_item, parent, false))
    }
}