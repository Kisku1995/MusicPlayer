package gabyshev.denis.musicplayer.utils

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Created by 1 on 18.07.2017.
 */
class RxBus {
    private val bus = PublishSubject.create<Any>()

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