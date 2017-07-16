package gabyshev.denis.musicplayer.service.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.NotificationCompat
import android.widget.RemoteViews
import gabyshev.denis.musicplayer.MainActivity
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.fragments.tracks.TracksHelper
import gabyshev.denis.musicplayer.service.TrackData
import gabyshev.denis.musicplayer.service.mediaplayer.RxMediaPlayerBus

/**
 * Created by Borya on 16.07.2017.
 */

class MediaPlayerNotification {
    companion object {
        private var instance: MediaPlayerNotification? = null

        fun instance(): MediaPlayerNotification? {
            if(instance == null) {
                instance = MediaPlayerNotification()
            }
            return instance!!
        }
    }

    fun buildNotification(context: Context, playbackStatus: PlaybackStatus, track: TrackData): Notification {
        val views: RemoteViews = RemoteViews(context.packageName, R.layout.media_player_notification)

        views.setTextViewText(R.id.title, track.title)
        views.setTextViewText(R.id.artist, track.artist)

        val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, track.albumId))
        if(bitmap != null) {
            views.setImageViewBitmap(R.id.image, bitmap)
        } else {
            views.setImageViewBitmap(R.id.image, TracksHelper.instance().getNoAlbumBitmap())
        }

        val intent = Intent(context, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.play)
                .setContent(views)
                .setContentIntent(pIntent)

       return notificationBuilder.build()
    }


}
