package gabyshev.denis.musicplayer.service

import android.media.AudioAttributes
import android.media.MediaPlayer

/**
 * Created by Borya on 15.07.2017.
 */

class MusicMediaPlayer {
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    companion object {
        private var instance: MusicMediaPlayer? = null

        fun instance(): MusicMediaPlayer? {
            if(instance == null) {
                instance = MusicMediaPlayer()
            }
            return instance!!
        }
    }

    init {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

        mediaPlayer.setAudioAttributes(audioAttributes)
    }

    fun playTrack(activeAudio: TrackData) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(activeAudio.data)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}
