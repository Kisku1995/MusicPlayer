package gabyshev.denis.musicplayer.service

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer

/**
 * Created by Borya on 15.07.2017.
 */

class MusicMediaPlayer: MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {


    // don't forget to release mediaplayer
    //private var audioManager: AudioManager = null
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var playlist: ArrayList<TrackData>? = null
    private var activeAudio: Int = 0

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

        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setAudioAttributes(audioAttributes)
    }

    fun playTrack() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(playlist?.get(activeAudio)?.data)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun setActiveAudioAndPlay(activeAudioPosition: Int) {
        activeAudio = activeAudioPosition
        playTrack()
    }

    fun setPlaylist(playlist: ArrayList<TrackData>) {
        this.playlist = playlist
    }

    override fun onCompletion(p0: MediaPlayer?) {
        activeAudio++

        if(activeAudio == playlist?.size) activeAudio = 0

        playTrack()
    }

    override fun onAudioFocusChange(focusState: Int) {

    }
}
