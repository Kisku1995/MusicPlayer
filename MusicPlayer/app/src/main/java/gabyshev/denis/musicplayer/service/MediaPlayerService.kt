package gabyshev.denis.musicplayer.service

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
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

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }










}
