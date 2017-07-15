package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.MusicMediaPlayer
import gabyshev.denis.musicplayer.service.TrackData

/**
 * Created by Borya on 15.07.2017.
 */

class TracksAdapter(private val context: Context, private val arrayTracks: ArrayList<TrackData>): RecyclerView.Adapter<TracksHolder>() {
    private val TAG = "TracksAdapter"

    init {
        MusicMediaPlayer.instance()!!.setPlaylist(arrayTracks)
    }

    override fun getItemCount(): Int = arrayTracks.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TracksHolder {
        return TracksHolder(LayoutInflater.from(context).inflate(R.layout.fragment_tracks_item, parent, false))
    }

    override fun onBindViewHolder(holder: TracksHolder?, position: Int) {
        holder?.bindTracksHolder(context, arrayTracks[position], position)
        holder?.itemView?.setOnClickListener {
            if(!MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
                Log.d(TAG, "service not running")
                context.startService(Intent(context, MediaPlayerService::class.java))
                MusicMediaPlayer.instance()!!.setActiveAudioAndPlay(position)
            } else {
                Log.d(TAG, "service running")
                MusicMediaPlayer.instance()!!.setActiveAudioAndPlay(position)
            }
        }
    }




}
