package gabyshev.denis.musicplayer.utils

import gabyshev.denis.musicplayer.events.ServiceActivity
import gabyshev.denis.musicplayer.fragments.player.PlayerFragment
import gabyshev.denis.musicplayer.utils.data.TrackData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Created by 1 on 18.07.2017.
 */
class RxBus {
    private val bus = PublishSubject.create<Any>()
    var track: ServiceActivity? = null

    fun send(event: Any) {
        bus.onNext(event)
    }

    fun toObservable(): Observable<Any> {
        return bus
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }
}