package gabyshev.denis.musicplayer.service.mediaplayer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.MainActivity
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.fragments.player.PlayerFragment
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.utils.TrackData
import javax.inject.Inject

/**
 * Created by Borya on 15.07.2017.
 */

class MusicMediaPlayer(val app: App): MediaPlayer.OnCompletionListener {
    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var playlist: ArrayList<TrackData>? = null
    private var activeAudio: Int = 0
    private var resumePosition: Int = 0

    private val TAG = "MusicMediaPlayer"

    var service: MediaPlayerService? = null

    @Inject lateinit var fragmentPlayer: PlayerFragment


    init {
        app.component.inject(this)
        initMediaPlayer()
    }

    fun initMediaPlayer() {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setAudioAttributes(audioAttributes)
    }

    fun playTrack() {
        if(service != null && playlist!!.size > 0) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.setDataSource(playlist?.get(activeAudio)?.data)
            mediaPlayer.prepare()
            mediaPlayer.start()
            updateFragmentPlayer()
            buildNotification()
        }
    }

    fun setPlaylist(playlist: ArrayList<TrackData>, position: Int) {
        this.playlist = playlist
        activeAudio = position
        playTrack()
    }

    override fun onCompletion(p0: MediaPlayer?) {
        nextTrack()
    }


    fun buildNotification(){
        val context: Context = service!!.applicationContext
        val views: RemoteViews = RemoteViews(context.packageName, R.layout.media_player_notification)
        val track = playlist!![activeAudio]

        views.setTextViewText(R.id.title, track.title)
        views.setTextViewText(R.id.artist, track.artist)

        val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, track.albumId))
        if(bitmap != null) {
            views.setImageViewBitmap(R.id.image, bitmap)
        } else {
            views.setImageViewBitmap(R.id.image, TracksHelper.instance().getNoAlbumBitmap())
        }

        views.setOnClickPendingIntent(R.id.previous, playbackAction(context, MediaPlayerStatus.PREVIOUS))

        if(isPlaying()) {
            views.setImageViewResource(R.id.playPause, R.drawable.pause)
            views.setOnClickPendingIntent(R.id.playPause, playbackAction(context, MediaPlayerStatus.PAUSE))
        } else {
            views.setImageViewResource(R.id.playPause, R.drawable.play)
            views.setOnClickPendingIntent(R.id.playPause, playbackAction(context, MediaPlayerStatus.RESUME))
        }

        views.setOnClickPendingIntent(R.id.next, playbackAction(context, MediaPlayerStatus.NEXT))
        views.setOnClickPendingIntent(R.id.close, playbackAction(context, MediaPlayerStatus.DESTROY))

        val intent = Intent(context, MainActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val pIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.music)
                .setContent(views)
                .setContentIntent(pIntent)
                .build()

        service!!.startForeground(1338, notificationBuilder)
    }

    fun playbackAction(context: Context, action: MediaPlayerStatus): PendingIntent? {
        val playbackAction = Intent(context, MediaPlayerService::class.java)
        playbackAction.action = action.action.toString()
        return PendingIntent.getService(context, action.action, playbackAction, 0)
    }

    fun getCurrentTrack(): TrackData? = playlist?.get(activeAudio)

    fun nextTrack() {
        activeAudio++
        if(activeAudio == playlist?.size) activeAudio = 0
        playTrack()
    }

    fun previousTrack() {
        activeAudio--
        if(activeAudio < 0) activeAudio = playlist!!.size - 1
        playTrack()
    }

    fun pauseTrack() {
        resumePosition = mediaPlayer.currentPosition
        mediaPlayer.pause()
        updateFragmentPlayer()
        buildNotification()
    }

    fun resumeTrack() {
        mediaPlayer.seekTo(resumePosition)
        mediaPlayer.start()
        updateFragmentPlayer()
        buildNotification()
    }

    fun onDestroy() {
        Log.d(TAG, "onDestroy")
        service = null

        if(fragmentPlayer.id != 0) {
            Log.d(TAG, "destroy completely")
            fragmentPlayer.destroyPlayer()
        }

        mediaPlayer.stop()


    }

    fun isPlaying() = mediaPlayer.isPlaying

    fun focusChange(focusState: Int) {
        when(focusState) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                Log.d(TAG, "AUDIOFOCUS_GAIN")
                if(mediaPlayer.isPlaying) {
                }
                mediaPlayer.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                Log.d(TAG, "AUDIOFOCUS_LOSS")
                if(mediaPlayer.isPlaying) {
                    pauseTrack()
                }
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                Log.d(TAG, "AUDIOFOCUS_GAIN_TRANSIENT")
                if(mediaPlayer.isPlaying) pauseTrack()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                if(mediaPlayer.isPlaying) mediaPlayer.setVolume(0.1f, 0.1f)
            }
        }
    }

    fun updateFragmentPlayer() {
        if(fragmentPlayer.id != 0) {
            fragmentPlayer.setPlayer()
        }
    }
}
