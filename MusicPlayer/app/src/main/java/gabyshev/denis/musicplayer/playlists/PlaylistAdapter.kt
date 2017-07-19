package gabyshev.denis.musicplayer.playlists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.data.TrackData

/**
 * Created by 1 on 19.07.2017.
 */
class PlaylistAdapter(private val context: Context, private var arrayTracks: ArrayList<TrackData>): RecyclerView.Adapter<PlaylistHolder>() {
    override fun getItemCount(): Int = arrayTracks.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaylistHolder {
        return PlaylistHolder(LayoutInflater.from(context).inflate(R.layout.activity_playlist_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.setHolder(arrayTracks[position])
    }
}