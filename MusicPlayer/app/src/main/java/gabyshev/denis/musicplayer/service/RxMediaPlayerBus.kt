package gabyshev.denis.musicplayer.service

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by Borya on 15.07.2017.
 */

class RxMediaPlayerBus {
    private val subject = PublishSubject.create<Int>()

    companion object {
        private var instance: RxMediaPlayerBus? = null

        fun instance(): RxMediaPlayerBus? {
            if(instance == null) {
                instance = RxMediaPlayerBus()
            }
            return instance!!
        }
    }

    fun setEvent(playerEvent: Int) {
        subject.onNext(playerEvent)
    }

    fun getEvent(): Observable<Int> = subject
}
