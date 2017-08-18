package gabyshev.denis.musicplayer.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import android.util.Log
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * Created by Borya on 15.07.2017.
 */

class MediaPlayerService: Service(), AudioManager.OnAudioFocusChangeListener {
    private val TAG = "MediaPlayerService"

    private var audioManager: AudioManager? = null

    @Inject lateinit var musicMediaPlayer: MusicMediaPlayer

    companion object {
        fun isRunning(context: Context, serviceClass: Class<*>): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.className)) {
                    return true
                }
            }
            return false
        }
    }


    override fun onCreate() {
        super.onCreate()
        (applicationContext as App).component.inject(this)
        musicMediaPlayer.service = this
        musicMediaPlayer.playTrack()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(!requestAudioFocus()) stopSelf()

        handleIncomingActions(intent)


        return super.onStartCommand(intent, flags, startId)
    }

    private fun requestAudioFocus(): Boolean {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val result: Int? = audioManager?.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;

    }

    private fun removeAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == audioManager?.abandonAudioFocus(this)
    }

    private fun handleIncomingActions(intent: Intent?) {
        if(intent != null) {
            val action = intent.action;
            Log.d(TAG, "action : ${action}")

            when(action) {
                MediaPlayerStatus.PREVIOUS.action.toString() -> {
                    musicMediaPlayer.previousTrack()
                }
                MediaPlayerStatus.PAUSE.action.toString() -> {
                    musicMediaPlayer.pauseTrack()
                }
                MediaPlayerStatus.RESUME.action.toString() -> {
                    musicMediaPlayer.resumeTrack()
                }
                MediaPlayerStatus.NEXT.action.toString() -> {
                    musicMediaPlayer.nextTrack()
                }
                MediaPlayerStatus.DESTROY.action.toString() -> {
                    stopSelf()
                }
            }
        }
    }

    override fun onDestroy() {
        removeAudioFocus()
        musicMediaPlayer.onDestroy()
        stopForeground(true)
        super.onDestroy()
    }

    override fun onAudioFocusChange(focusState: Int) {
        musicMediaPlayer.focusChange(focusState)
    }
}

