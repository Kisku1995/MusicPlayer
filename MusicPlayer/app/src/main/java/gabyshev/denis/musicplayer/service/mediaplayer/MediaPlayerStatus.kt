package gabyshev.denis.musicplayer.service.mediaplayer

/**
 * Created by Borya on 18.07.2017.
 */

enum class MediaPlayerStatus(val action: Int) {
    CREATE(-1),
    PREVIOUS(0),
    PAUSE(1),
    RESUME(2),
    NEXT(3),
    DESTROY(4)
}

data class MediaPlayerStatusEvent(val action: Int)