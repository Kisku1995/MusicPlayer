package gabyshev.denis.musicplayer.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.IBinder
import android.util.Log
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.events.MediaPlayerStatusEvent
import gabyshev.denis.musicplayer.service.activityplayer.RxServiceActivity
import gabyshev.denis.musicplayer.service.activityplayer.ServiceActivity
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import gabyshev.denis.musicplayer.service.mediaplayer.RxMediaPlayerBus
import gabyshev.denis.musicplayer.utils.RxBus
import javax.inject.Inject
import io.reactivex.disposables.CompositeDisposable



/**
 * Created by Borya on 15.07.2017.
 */

class MediaPlayerService: Service(), AudioManager.OnAudioFocusChangeListener {
    private val TAG = "MediaPlayerService"

    private var musicMediaPlayer: MusicMediaPlayer = MusicMediaPlayer(this)
    private var audioManager: AudioManager? = null

    private var subsriptions = CompositeDisposable()

    @Inject lateinit var rxBus: RxBus

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
        RxListener()
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
                "0" -> {
                    musicMediaPlayer.previousTrack()
                }
                "1" -> {
                    musicMediaPlayer.pauseTrack()
                }
                "2" -> {
                    musicMediaPlayer.resumeTrack()
                }
                "3" -> {
                    musicMediaPlayer.nextTrack()
                }
                "4" -> {
                    stopSelf()
                }
            }

            if(action == null) {
                RxServiceActivity.instance()?.setServiceActivity(ServiceActivity(null, -1))
            }
        }
    }

    private fun RxListener() {
        RxServiceActivity.instance()?.getActivityService()?.subscribe({
            if(it == 0) {
//                musicMediaPlayer.pauseTrack()
//                musicMediaPlayer.buildNotification(this, musicMediaPlayer.getCurrentTrack()!!)
            }
            if(it == 1){
                musicMediaPlayer.resumeTrack()
            }
        })


        subsriptions.add(
                rxBus.toObservable()
                .subscribe( {
                    if(it is MediaPlayerStatusEvent && it.action == 0) {
                        handleIncomingActions(getAction(1)) // pause
                    }
                })
        )
    }

    private fun getAction(action: Int): Intent {
        val playbackAction = Intent(this, MediaPlayerService::class.java)
        playbackAction.action = action.toString()
        return playbackAction
    }

    override fun onDestroy() {
        removeAudioFocus()
        musicMediaPlayer.onDestroy()
        RxServiceActivity.instance()?.getActivityService()?.onComplete()
        RxServiceActivity.instance()?.createActivityService()
        subsriptions.dispose()
        stopForeground(true)
        super.onDestroy()
    }

    override fun onAudioFocusChange(focusState: Int) {
        when(focusState) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                Log.d(TAG, "AUDIOFOCUS_GAIN")
                if(!musicMediaPlayer.mediaPlayer.isPlaying) {
                    musicMediaPlayer.resumeTrack()
                }
                musicMediaPlayer.mediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                Log.d(TAG, "AUDIOFOCUS_LOSS")
                if(musicMediaPlayer.mediaPlayer.isPlaying) {
                    musicMediaPlayer.pauseTrack()
             }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                Log.d(TAG, "AUDIOFOCUS_GAIN_TRANSIENT")
                if(musicMediaPlayer.mediaPlayer.isPlaying) musicMediaPlayer.pauseTrack()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                if(musicMediaPlayer.mediaPlayer.isPlaying) musicMediaPlayer.mediaPlayer.setVolume(0.1f, 0.1f)
            }
        }
    }
}

