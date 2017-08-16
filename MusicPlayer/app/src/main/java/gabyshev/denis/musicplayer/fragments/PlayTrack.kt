package gabyshev.denis.musicplayer.fragments

import android.content.Context
import android.content.Intent
import gabyshev.denis.musicplayer.events.TracksArrayPosition
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatusEvent
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TrackData
import io.reactivex.disposables.CompositeDisposable
import java.util.*

/**
 * Created by Borya on 16.08.2017.
 */
object PlayTrack {
    fun playTrack(context: Context,
            arrayObject: ArrayList<TrackData>,
            rxBus: RxBus,
            subscriptions: CompositeDisposable,
            position: Int) {
        if(!MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
            context.startService(Intent(context, MediaPlayerService::class.java))

            subscriptions.add(
                    rxBus.toObservable()
                            .subscribe({
                                if(it is MediaPlayerStatusEvent && it.action == MediaPlayerStatus.CREATE.action) {
                                    rxBus.send(TracksArrayPosition(arrayObject, position))
                                }
                            })
            )
        } else {
            rxBus.send(TracksArrayPosition(arrayObject, position))
        }
    }
}