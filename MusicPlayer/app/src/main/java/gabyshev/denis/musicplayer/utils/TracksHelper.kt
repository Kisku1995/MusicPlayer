package gabyshev.denis.musicplayer.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.AppCompatDrawableManager
import android.util.Log
import android.view.View
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.category.CategoryActivity
import gabyshev.denis.musicplayer.utils.data.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Borya on 15.07.2017.
 */
class TracksHelper {
    private val TAG = "TracksHelper"

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

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toLong()
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val duration = convertDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)).toLong())
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

    fun scanForAlbums(context: Context): ArrayList<Album> {
        var arrayAlbums: ArrayList<Album> = ArrayList<Album>()

        val uri: Uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART)

        val sortOrder = "${MediaStore.Audio.Media.ALBUM} ASC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, sortOrder)

        if(cursor != null) {
            cursor.moveToFirst()
            while(!cursor.isAfterLast) {
                val id: Int = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)).toInt()
                val album: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
                val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
                val cover: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))

                cursor.moveToNext()

                arrayAlbums.add(Album(id, album, artist, cover))
            }

            cursor.close()
        }

        return arrayAlbums
    }

    fun scanForArtists(context: Context): ArrayList<Artist> {
        var arrayArtists: ArrayList<Artist> = ArrayList<Artist>()

        val uri: Uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )

        val sortOrder = "${MediaStore.Audio.Artists.ARTIST} ASC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, sortOrder)

        if(cursor != null) {
            cursor.moveToFirst()
            while(!cursor.isAfterLast) {
                val id: Int = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)).toInt()
                val artist: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST))
                val albumsCount: Int = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)).toInt()
                val tracksCount: Int = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)).toInt()

                cursor.moveToNext()

                arrayArtists.add(Artist(id, artist, albumsCount, tracksCount))
            }

            cursor.close()
        }

        return arrayArtists
    }

    fun scanForGenres(context: Context): ArrayList<Genre> {
        val arrayGenres: ArrayList<Genre> = ArrayList<Genre>()

        val uri: Uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME
        )

        val sortOrder = "${MediaStore.Audio.Genres.NAME} ASC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, sortOrder)

        if(cursor != null) {
            cursor.moveToFirst()
            while(!cursor.isAfterLast) {
                val id: Int = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres._ID)).toInt()
                val name: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME))

                cursor.moveToNext()

                arrayGenres.add(Genre(id, name))
            }

            cursor.close()
        }

        return arrayGenres
    }

    fun scanForPlaylists(context: Context): ArrayList<Playlist> {
        var arrayPlaylists: ArrayList<Playlist> = ArrayList<Playlist>()

        val uri: Uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME
        )

        val sortOrder = "${MediaStore.Audio.Playlists.NAME} ASC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, sortOrder)

        if(cursor != null) {
            cursor.moveToFirst()
            while(!cursor.isAfterLast) {
                val id: Int = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)).toInt()
                val name: String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))

                cursor.moveToNext()

                arrayPlaylists.add(Playlist(id, name))
            }

            cursor.close()
        }

        return arrayPlaylists
    }

    fun scanForCategory(context: Context, categoryId: Int, category: Int): ArrayList<TrackData> {
        // 0 - albums, 1 - artists, 2 - genres
        var arrayTrackData: ArrayList<TrackData> = ArrayList<TrackData>()


        var uri: Uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        if(category == 2) {
            uri = android.provider.MediaStore.Audio.Genres.Members.getContentUri("external", categoryId.toLong())
        }

        val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID)

        var selection = "${MediaStore.Audio.Media.IS_MUSIC}  != 0"
        if(category == 0) {
            selection = "${MediaStore.Audio.Albums.ALBUM_ID} == $categoryId"
        }
        if(category == 1) {
            selection = "${MediaStore.Audio.Media.ARTIST_ID} == $categoryId"
        }

        val sortOrder = "${MediaStore.Audio.AudioColumns.TITLE} COLLATE LOCALIZED ASC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        if (cursor != null) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toLong()
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val duration = convertDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)).toLong())
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

    private fun convertDuration(duration: Long): String {
        var out: String = "00:00"
        var hours: Long = 0

        try {
            hours = (duration / 3600000)
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            return out
        }

        val remaining_minutes = (duration - hours * 3600000) / 60000
        var minutes = remaining_minutes.toString()
        if (minutes.contains("0")) {
            minutes = "00"
        }
        val remaining_seconds = duration - hours * 3600000 - remaining_minutes * 60000
        var seconds = remaining_seconds.toString()
        if (seconds.length < 2) {
            seconds = "00"
        } else {
            seconds = seconds.substring(0, 2)
        }

        if (hours > 0) {
            out = hours.toString() + ":" + minutes + ":" + seconds
        } else {
            out = minutes + ":" + seconds
        }

        return out
    }

    fun getAlbumImagePath(context: Context, albumId: Int): String? {
        val uri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
        val selection = MediaStore.Audio.Albums._ID + "=?"
        val args = arrayOf<String>(albumId.toString())

        val cursor = context.contentResolver.query(uri, projection, selection, args, null)

        var pathOfAlbum: String? = null

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                pathOfAlbum = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
                Log.i(TAG, "PATH : " + pathOfAlbum)
            }
        }

        cursor.close()

        return pathOfAlbum
    }

    fun getNoAlbumBitmap(): Bitmap {
        val colorBitmap = Bitmap.createBitmap(192, 192, Bitmap.Config.ARGB_8888)
        val colors = arrayOf("#f44336", "#e91e63", "#9c27b0", "#673ab7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722")
        colorBitmap.eraseColor(Color.parseColor(colors[Random().nextInt(colors.size)]))
        return colorBitmap
    }

    fun getRoundedShape(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = 0xff424242.toInt()
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
                (bitmap.width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    fun setBackground(context: Context, view: View, position: Int) {
        if(position % 2 == 0) view.background = AppCompatDrawableManager.get().getDrawable(context, R.color.track_even)
        else view.background = AppCompatDrawableManager.get().getDrawable(context, R.color.track_odd)
    }

    fun startCategory(context: Context, category: Category) {
        val bundle: Bundle = Bundle()
        bundle.putInt("categoryId", category.cateogryId)
        bundle.putInt("category", category.category)
        bundle.putString("title", category.title)

        val intent: Intent = Intent(context, CategoryActivity::class.java)
        intent.putExtras(bundle)

        context.startActivity(intent)
    }




}
