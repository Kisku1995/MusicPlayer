package gabyshev.denis.musicplayer.fragments.tracks

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import org.jetbrains.anko.find

/**
 * Created by Borya on 15.07.2017.
 */
class TracksHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.find(R.id.title)
    private val artist: TextView = view.find(R.id.artist)
    private val duration: TextView = view.find(R.id.duration)

    fun bindTracksHolder(trackData: TrackData) {
        title.text = trackData.title
        artist.text = trackData.artist
        duration.text = trackData.duration.toString()
    }
}