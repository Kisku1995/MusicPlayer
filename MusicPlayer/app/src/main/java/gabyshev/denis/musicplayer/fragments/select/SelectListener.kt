package gabyshev.denis.musicplayer.fragments.select

import gabyshev.denis.musicplayer.utils.data.TrackData

/**
 * Created by 1 on 18.07.2017.
 */
interface SelectListener {
    fun startSelect()
    fun stopSelect()
    fun countSelect(count: String)
}