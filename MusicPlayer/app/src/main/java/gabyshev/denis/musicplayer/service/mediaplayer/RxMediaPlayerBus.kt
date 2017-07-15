package gabyshev.denis.musicplayer.service.mediaplayer

import gabyshev.denis.musicplayer.service.TrackData
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by Borya on 15.07.2017.
 */

class RxMediaPlayerBus {
    private val subject = PublishSubject.create<Any>()
    private val position = PublishSubject.create<Int>()

    companion object {
        private var instance: RxMediaPlayerBus? = null

        fun instance(): RxMediaPlayerBus? {
            if(instance == null) {
                instance = RxMediaPlayerBus()
            }
            return instance!!
        }
    }

    fun setActiveAudioAndPlay(playerEvent: Int) {
        position.onNext(playerEvent)
    }

    fun getActiveAudioAndPlay(): PublishSubject<Int>? = position

    fun setPlaylist(playlist: ArrayList<TrackData>) {
        subject.onNext(playlist)
    }

    fun getPlaylist(): PublishSubject<Any>? {
        return subject
    }


}
