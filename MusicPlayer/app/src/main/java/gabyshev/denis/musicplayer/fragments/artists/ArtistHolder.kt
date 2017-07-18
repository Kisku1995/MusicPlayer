package gabyshev.denis.musicplayer.fragments.artists

import android.content.Context
import android.support.v7.widget.AppCompatDrawableManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Artist
import kotlinx.android.synthetic.main.fragment_albums_item.*
import org.jetbrains.anko.find

/**
 * Created by 1 on 18.07.2017.
 */
class ArtistHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val artist: TextView = view.find(R.id.artist)
    private val albumsCount: TextView = view.find(R.id.albumsCount)
    private val tracksCount: TextView = view.find(R.id.tracksCount)

    fun setHolder(context: Context, artist: Artist, position: Int) {
        this.artist.text = artist.artist
        albumsCount.text = "${artist.albumCount.toString()} ${context.getString(R.string.albums)}"
        tracksCount.text = "${artist.trackCount.toString()} ${context.getString(R.string.tracks)}"

        TracksHelper.instance().setBackground(context, view, position)
    }


}