package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.service.MusicMediaPlayer
import gabyshev.denis.musicplayer.service.TrackData

/**
 * Created by Borya on 15.07.2017.
 */

class TracksAdapter(context: Context, arrayTracks: ArrayList<TrackData>): RecyclerView.Adapter<TracksHolder>() {
    private val mArrayTracks: ArrayList<TrackData> = arrayTracks
    private val mContext: Context = context

    init {
        MusicMediaPlayer.instance()!!.setPlaylist(mArrayTracks)
    }

    override fun getItemCount(): Int = mArrayTracks.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TracksHolder {
        return TracksHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_tracks_item, parent, false))
    }

    override fun onBindViewHolder(holder: TracksHolder?, position: Int) {
        holder?.bindTracksHolder(mContext, mArrayTracks[position], position)
        holder?.itemView?.setOnClickListener {
            MusicMediaPlayer.instance()!!.setActiveAudioAndPlay(position)
        }
    }




}
