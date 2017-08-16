package gabyshev.denis.musicplayer.utils

/**
 * Created by Borya on 15.08.2017.
 */
data class Album(override val id: Int, val album: String, val artist: String, val cover: String?) : Identifier(id = id)
data class Artist(override val id: Int, val artist: String, val albumCount: Int, val trackCount: Int) : Identifier(id = id)
data class Genre(override val id: Int, val name: String) : Identifier(id = id)
data class Playlist(override val id: Int, val name: String) : Identifier(id = id)
data class TrackData(override val id: Int, val title: String, val artist: String, val data: String, val duration: String, val albumId: Int) : Identifier(id = id)

abstract class Identifier(open val id: Int)