package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.*
import gabyshev.denis.musicplayer.fragments.RecyclerViewSelectAbstract
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.utils.data.TrackData
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.utils.RxBus
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by Borya on 15.07.2017.
 */

class TracksAdapter(private val context: Context, private var arrayTracks: ArrayList<TrackData>, private val rxBus: RxBus, private var subscriptions: CompositeDisposable)
    : RecyclerViewSelectAbstract<TrackData, TracksHolder>(context, arrayTracks, rxBus, subscriptions) {

    private val TAG = "TracksAdapter"

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TracksHolder {
        return TracksHolder(LayoutInflater.from(context).inflate(R.layout.fragment_tracks_item, parent, false))
    }

    override fun onBindViewHolder(holder: TracksHolder?, position: Int) {
        holder?.bindTracksHolder(context, arrayTracks[position], position)
        holder?.itemView?.setOnClickListener {
            if(selectedObject.size == 0) playTrack(position)
            else {
                checkHolder(holder, position)
            }

        }

        holder?.itemView?.setOnLongClickListener(View.OnLongClickListener {
            if(selectedObject.size == 0) {
                selectListener.startSelect()
            }

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

    override fun addSelecting() {

//        selectListener.stopSelect()
//        notifyDataSetChanged()
//        for(item in selectedObject) {
//            Log.d(TAG, "${item.artist} : ${item.title}")
//        }
//        selectedObject.clear()

    }

    override fun isContainsTrack(position: Int): Boolean {
        val trackId: Long = arrayTracks[position].id

        for(item in selectedObject) {
            if(trackId == item.id) {
                selectedObject.remove(item)
                if(selectedObject.size == 0) {
                    selectListener.stopSelect()
                }
                return true
            }
        }

        selectedObject.add(arrayTracks[position])

        return false
    }
}