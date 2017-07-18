package gabyshev.denis.musicplayer.utils

import gabyshev.denis.musicplayer.utils.data.TrackData

/**
 * Created by 1 on 18.07.2017.
 */
interface SelectListener {
    fun startSelect()
    fun addToSelect(tracks: ArrayList<TrackData>)
    fun stopSelect(action: Int) // action 0 - insert to playlist, 1 - cancel
}