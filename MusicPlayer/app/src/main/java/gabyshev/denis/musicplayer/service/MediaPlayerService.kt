package gabyshev.denis.musicplayer.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import gabyshev.denis.musicplayer.service.activityplayer.RxServiceActivity
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import gabyshev.denis.musicplayer.service.mediaplayer.RxMediaPlayerBus

/**
 * Created by Borya on 15.07.2017.
 */

class MediaPlayerService: Service() {
    private val TAG = "MediaPlayerService"

    private var musicMediaPlayer: MusicMediaPlayer = MusicMediaPlayer(this)

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

    init {
        RxListener()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handleIncomingActions(intent)
        return super.onStartCommand(intent, flags, startId)
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
                    musicMediaPlayer.buildNotification(this, musicMediaPlayer.getCurrentTrack()!!)
                }
                "2" -> {
                    musicMediaPlayer.resumeTrack()
                    musicMediaPlayer.buildNotification(this, musicMediaPlayer.getCurrentTrack()!!)
                }
                "3" -> {
                    musicMediaPlayer.nextTrack()
                }
                "4" -> {
                    stopSelf()
                }
            }
        }
    }

    private fun RxListener() {
        RxServiceActivity.instance()?.getActivityService()?.subscribe({
            if(it == 0) {
                musicMediaPlayer.pauseTrack()
                musicMediaPlayer.buildNotification(this, musicMediaPlayer.getCurrentTrack()!!)
            } else {
                musicMediaPlayer.resumeTrack()
                musicMediaPlayer.buildNotification(this, musicMediaPlayer.getCurrentTrack()!!)
            }
        })
    }

    override fun onDestroy() {
        musicMediaPlayer.onDestroy()
        RxServiceActivity.instance()?.getActivityService()?.onComplete()
        RxServiceActivity.instance()?.createActivityService()
        stopForeground(true)
        super.onDestroy()
    }
}

