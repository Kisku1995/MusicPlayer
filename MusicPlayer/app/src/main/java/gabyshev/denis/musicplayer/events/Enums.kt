package gabyshev.denis.musicplayer.events

import gabyshev.denis.musicplayer.utils.Album
import gabyshev.denis.musicplayer.utils.Artist
import gabyshev.denis.musicplayer.utils.Genre

/**
 * Created by Borya on 12.08.2017.
 */

enum class CategoryID(val category: Int) {
    ALBUMS(0),
    ARTISTS(1),
    GENRES(2);

    companion object {
        fun getType(theClass: Any): Int {
            return when (theClass) {
                Album::class.java -> 0
                Artist::class.java -> 1
                Genre::class.java -> 2
                else -> throw Exception("Wrong class")
            }
        }
    }
}