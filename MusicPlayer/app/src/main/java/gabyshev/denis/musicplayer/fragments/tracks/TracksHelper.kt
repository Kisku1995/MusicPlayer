package gabyshev.denis.musicplayer.fragments.tracks

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import java.util.ArrayList

/**
 * Created by Borya on 15.07.2017.
 */
class TracksHelper {
    companion object {
        private var instance: TracksHelper? = null

        fun instance(): TracksHelper {
            if(instance == null) {
                instance = TracksHelper()
            }
            return instance!!
        }
    }

    fun scanMP3Files(context: Context): ArrayList<TrackData> {
        var arrayTrackData: ArrayList<TrackData> = ArrayList<TrackData>()

        val uri: Uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}  != 0"
        val sortOrder = "${MediaStore.Audio.AudioColumns.TITLE} COLLATE LOCALIZED ASC"

        val cursor: Cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        cursor.moveToFirst()
        while(!cursor.isAfterLast) {
            val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toLong()
            val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)).toLong()
            val albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toInt()

            cursor.moveToNext()

            if(data != null && (data.endsWith(".mp3") || data.endsWith(".MP3"))) {
                arrayTrackData.add(TrackData(id, title, artist, data, duration, albumId))
            }
        }

        cursor.close()

        return arrayTrackData
    }

}
