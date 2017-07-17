package gabyshev.denis.musicplayer.service.mediaplayer

import android.app.PendingIntent
import android.app.Service
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
import gabyshev.denis.musicplayer.MainActivity
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.fragments.tracks.TracksHelper
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.TrackData
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by Borya on 15.07.2017.
 */

class MusicMediaPlayer(private val service: Service): MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {
    // don't forget to release mediaplayer
    //private var audioManager: AudioManager = null
    var mediaPlayer: MediaPlayer = MediaPlayer()
    private var playlist: ArrayList<TrackData>? = null
    private var activeAudio: Int = 0

    private val TAG = "MusicMediaPlayer"

    init {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

        mediaPlayer.setOnCompletionListener(this)
        mediaPlayer.setAudioAttributes(audioAttributes)

        RxListener()
    }

    fun playTrack() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(playlist?.get(activeAudio)?.data)
        buildNotification(service.applicationContext, playlist!![activeAudio])
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun setActiveAudioAndPlay(activeAudioPosition: Int) {
        activeAudio = activeAudioPosition
        playTrack()
    }

    fun setPlaylist(playlist: ArrayList<TrackData>) {
        this.playlist = playlist

        for(item in playlist) {
            Log.d(TAG, item.title)
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {
        nextTrack()
    }

    override fun onAudioFocusChange(focusState: Int) {

    }

    private fun RxListener() {
        Log.d(TAG, "RxListener")
        RxMediaPlayerBus.instance()?.getPlaylist()?.subscribe({
            Log.d(TAG, "getPlaylis")
            @Suppress("UNCHECKED_CAST")
            setPlaylist(it as? ArrayList<TrackData> ?: ArrayList<TrackData>())
        })

        RxMediaPlayerBus.instance()?.getActiveAudioAndPlay()?.subscribe({
            Log.d(TAG, "getActiveAudioAndPlay")
            setActiveAudioAndPlay(it)
            Log.d(TAG, "POSITION : ${it}")
        })
    }

    fun buildNotification(context: Context, track: TrackData){
        val views: RemoteViews = RemoteViews(context.packageName, R.layout.media_player_notification)

        views.setTextViewText(R.id.title, track.title)
        views.setTextViewText(R.id.artist, track.artist)

        val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, track.albumId))
        if(bitmap != null) {
            views.setImageViewBitmap(R.id.image, bitmap)
        } else {
            views.setImageViewBitmap(R.id.image, TracksHelper.instance().getNoAlbumBitmap())
        }

        views.setOnClickPendingIntent(R.id.previous, playbackAction(context, 0))
        views.setOnClickPendingIntent(R.id.playPause, playbackAction(context, 1))
        views.setOnClickPendingIntent(R.id.next, playbackAction(context, 2))
        views.setOnClickPendingIntent(R.id.close, playbackAction(context, 3))

        val intent = Intent(context, MainActivity::class.java)

        val pIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.play)
                .setContent(views)
                .setContentIntent(pIntent)

        service.startForeground(1338, notificationBuilder.build())
    }

    fun playbackAction(context: Context, action: Int): PendingIntent? {
        val playbackAction = Intent(context, MediaPlayerService::class.java)
        playbackAction.action = action.toString()
        return PendingIntent.getService(context, action, playbackAction, 0)
    }

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

    fun onDestroy() {
        RxMediaPlayerBus.instance()?.getPlaylist()?.onComplete()
        RxMediaPlayerBus.instance()?.getActiveAudioAndPlay()?.onComplete()
        RxMediaPlayerBus.instance()?.createAgain()
    }
}
