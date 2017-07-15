package gabyshev.denis.musicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

/**
 * Created by Borya on 15.07.2017.
 */

class MediaPlayerService : Service() {


    init {

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


}
