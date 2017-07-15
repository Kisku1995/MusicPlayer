package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.support.v7.widget.AppCompatDrawableManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.service.TrackData
import org.jetbrains.anko.find

/**
 * Created by Borya on 15.07.2017.
 */
class TracksHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val mView: View = view
    private val title: TextView = view.find(R.id.title)
    private val artist: TextView = view.find(R.id.artist)
    private val duration: TextView = view.find(R.id.duration)

    fun bindTracksHolder(context: Context, trackData: TrackData, position: Int) {
        title.text = trackData.title
        artist.text = trackData.artist
        duration.text = trackData.duration
        setBackground(context, position)
    }

    private fun setBackground(context: Context, position: Int) {
        if(position % 2 == 0) mView.background = AppCompatDrawableManager.get().getDrawable(context, R.color.track_even)
         else mView.background = AppCompatDrawableManager.get().getDrawable(context, R.color.track_odd)
    }
}