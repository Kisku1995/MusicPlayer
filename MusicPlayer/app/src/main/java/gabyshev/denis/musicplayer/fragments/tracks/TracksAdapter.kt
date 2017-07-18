package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.MediaPlayerStatusEvent
import gabyshev.denis.musicplayer.events.TrackPosition
import gabyshev.denis.musicplayer.events.TracksArray
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.utils.data.TrackData
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.SelectListener
import gabyshev.denis.musicplayer.utils.TracksHelper
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

/**
 * Created by Borya on 15.07.2017.
 */

class TracksAdapter(private val context: Context, private val arrayTracks: ArrayList<TrackData>, private val rxBus: RxBus, private val subscriptions: CompositeDisposable): RecyclerView.Adapter<TracksHolder>() {
    private val TAG = "TracksAdapter"
    var selectListener: SelectListener = context as SelectListener

    var selectedTracks = ArrayList<TrackData>()

    override fun getItemCount(): Int = arrayTracks.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TracksHolder {
        return TracksHolder(LayoutInflater.from(context).inflate(R.layout.fragment_tracks_item, parent, false))
    }

    override fun onBindViewHolder(holder: TracksHolder?, position: Int) {
        holder?.bindTracksHolder(context, arrayTracks[position], position)
        holder?.itemView?.setOnClickListener {
            if(selectedTracks.size == 0) playTrack(position)
            else {
                checkHolder(holder, position)
            }

        }

        holder?.itemView?.setOnLongClickListener(View.OnLongClickListener {
            checkHolder(holder, position)
            true
        })
    }
    
    private fun playTrack(position: Int) {
        if(!MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
            Log.d(TAG, "service not running")

            context.startService(Intent(context, MediaPlayerService::class.java))

            subscriptions.add(
                    rxBus.toObservable()
                            .subscribe({
                                if(it is MediaPlayerStatusEvent && it.action == MediaPlayerStatus.CREATE.action) {
                                    rxBus.send(TracksArray(arrayTracks))
                                    rxBus.send(TrackPosition(position))
                                }
                            })
            )
        } else {
            Log.d(TAG, "service running")
            rxBus.send(TracksArray(arrayTracks))
            rxBus.send(TrackPosition(position))
        }
    }

    private fun checkHolder(holder: TracksHolder, position: Int) {
        if(isContainsTrack(position)) {
            TracksHelper.instance().setBackground(context, holder.itemView, position)
        } else {
            TracksHelper.instance().setSelectedBackground(context, holder.itemView, position)
        }
    }

    private fun isContainsTrack(position: Int): Boolean {
        val trackId: Long = arrayTracks[position].id

        for(item in selectedTracks) {
            if(trackId == item.id) {
                selectedTracks.remove(item)
                return true
            }
        }

        selectedTracks.add(arrayTracks[position])

        return false
    }




}
