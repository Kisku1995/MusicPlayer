package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.*
import gabyshev.denis.musicplayer.fragments.PlayTrack
import gabyshev.denis.musicplayer.fragments.RecyclerViewSelectAbstract
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.utils.TrackData
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import gabyshev.denis.musicplayer.utils.RxBus
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Borya on 15.07.2017.
 */

class TracksAdapter(private val context: Context,
                    private var arrayTracks: ArrayList<TrackData>,
                    private val rxBus: RxBus,
                    private val subscriptions: CompositeDisposable)
    : RecyclerViewSelectAbstract<TrackData, TracksHolder>(context, arrayTracks, rxBus, subscriptions) {

    override val classToken = TrackData::class.java

    @Inject lateinit var musicPlayer: MusicMediaPlayer

    init {
        (context.applicationContext as App).component.inject(this)
       subscribe()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TracksHolder {
        return TracksHolder(LayoutInflater.from(context).inflate(R.layout.fragment_tracks_item, parent, false))
    }

    override fun onBindViewHolder(holder: TracksHolder, position: Int) {
        holder.bindTracksHolder(context, arrayTracks[position], position)

        holderTracks(holder, position, {PlayTrack.playTrack(context, arrayTracks, musicPlayer, position)})
    }
}