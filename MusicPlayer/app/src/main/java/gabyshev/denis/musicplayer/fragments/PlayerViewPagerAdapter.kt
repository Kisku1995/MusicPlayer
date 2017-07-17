package gabyshev.denis.musicplayer.fragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import gabyshev.denis.musicplayer.fragments.playlists.PlaylistsFragment
import gabyshev.denis.musicplayer.fragments.tracks.TracksFragment

/**
 * Created by Borya on 15.07.2017.
 */
class PlayerViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val menuItems = arrayOf("tracks", "playlists", "albums", "artists", "genres")

    override fun getItem(position: Int): Fragment {
        if(position == 0)
        return TracksFragment.instance()!!
        else
            return PlaylistsFragment()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return menuItems[position]
    }

    override fun getCount(): Int = 5
}