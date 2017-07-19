package gabyshev.denis.musicplayer.playlists

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.TrackData
import android.content.ContentResolver
import android.widget.Toast
import gabyshev.denis.musicplayer.R
import org.jetbrains.anko.toast



/**
 * Created by 1 on 19.07.2017.
 */
class PlaylistHelper {

    companion object {
        private var instance: PlaylistHelper? = null

        fun instance(): PlaylistHelper {
            if(instance == null) {
                instance = PlaylistHelper()
            }
            return instance!!
        }
    }


    fun createPlaylist(context: Context, name: String) {
        val resolver = context.contentResolver
        val values = ContentValues(1)
        values.put(MediaStore.Audio.Playlists.NAME, name)
        val uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        resolver.insert(uri, values)

        context.toast(context.getString(R.string.playlist_created))
    }

    fun getPlaylistTracks(context: Context, id: Long): ArrayList<TrackData> {
        var arrayTrackData: ArrayList<TrackData> = ArrayList<TrackData>()

        val uri: Uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC}  != 0"
        val sortOrder = "${MediaStore.Audio.AudioColumns.TITLE} COLLATE LOCALIZED ASC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toLong()
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val duration = TracksHelper.instance().convertDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)).toLong())
                val albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toInt()

                cursor.moveToNext()

                if (data != null && (data.endsWith(".mp3") || data.endsWith(".MP3"))) {
                    arrayTrackData.add(TrackData(id, title, artist, data, duration, albumId))
                }
            }

            cursor.close()
        }

        return arrayTrackData
    }

    fun addTracksToPlaylist(id: Long, tracks: List<TrackData>, context: Context) {
        val count = getPlaylistSize(id, context)
        val values = arrayOfNulls<ContentValues>(tracks.size)
        for (i in tracks.indices) {
            values[i] = ContentValues()
            values[i]!!.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, i + count + 1)
            values[i]!!.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, tracks[i].id)
        }
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)
        val resolver = context.contentResolver
        resolver.bulkInsert(uri, values)
        resolver.notifyChange(Uri.parse("content://media"), null)

        context.toast(context.getString(R.string.playlist_updated))
    }

    private fun getPlaylistSize(id: Long, context: Context): Int {
        val count = java.util.ArrayList<Long>()
        val uri = MediaStore.Audio.Playlists.Members.getContentUri("external", id)

        val projection = arrayOf(MediaStore.Audio.Playlists.Members._ID)
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"

        val cursor: Cursor? = context.getContentResolver().query(uri, projection, selection, null, null)

        if (cursor != null) {
            cursor.moveToFirst()

            while (!cursor.isAfterLast()) {
                count.add(java.lang.Long.parseLong(cursor.getString(0)))
                cursor.moveToNext()
            }

            cursor.close()
        }

        return count.size
    }

    fun deletePlaylist(context: Context, id: Long) {
        val playlistId = id.toString()
        val resolver = context.contentResolver
        val where = MediaStore.Audio.Playlists._ID + "=?"
        val whereVal = arrayOf(playlistId)
        resolver.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, where, whereVal)
    }

}