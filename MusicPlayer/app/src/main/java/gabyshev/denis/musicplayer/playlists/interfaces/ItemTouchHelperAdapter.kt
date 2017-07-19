package gabyshev.denis.musicplayer.playlists.interfaces

/**
 * Created by Denis on 13.03.2017.
 */

interface ItemTouchHelperAdapter {
    fun onItemMove(oldPos: Int, newPos: Int): Boolean
    fun onItemSwipe(pos: Int)
}
