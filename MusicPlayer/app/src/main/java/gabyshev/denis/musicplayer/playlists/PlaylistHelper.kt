package gabyshev.denis.musicplayer.playlists

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import gabyshev.denis.musicplayer.utils.TracksHelper

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
    }
}