package gabyshev.denis.musicplayer.fragments

import android.content.Context
import android.content.Intent
import android.util.Log
import gabyshev.denis.musicplayer.events.TracksArrayPosition
import gabyshev.denis.musicplayer.player.PlayerActivity
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TrackData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by Borya on 16.08.2017.
 */
object PlayTrack {
    private val TAG = "PlayTrack"

    fun playTrack(context: Context,
            arrayObject: ArrayList<TrackData>,
            musicPlayer: MusicMediaPlayer,
            position: Int) {

        Log.d(TAG, "service is running : ${MediaPlayerService.isRunning(context, MediaPlayerService::class.java)}")

        if(!MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
            context.startService(Intent(context, MediaPlayerService::class.java))
        }

        context.startActivity(Intent(context, PlayerActivity::class.java))
        musicPlayer.setPlaylist(arrayObject, position)
    }
}