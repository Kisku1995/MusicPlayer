package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.TrackData
import org.jetbrains.anko.find

/**
 * Created by Borya on 15.07.2017.
 */
class TracksHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val mView: View = view
    private val title: TextView = view.find(R.id.title)
    private val artist: TextView = view.find(R.id.artist)
    private val duration: TextView = view.find(R.id.duration)

    fun bindTracksHolder(context: Context, trackData: TrackData, position: Int) {
        title.text = trackData.title
        artist.text = trackData.artist
        duration.text = trackData.duration
        TracksHelper.instance().setBackground(context, view, position)
    }
}