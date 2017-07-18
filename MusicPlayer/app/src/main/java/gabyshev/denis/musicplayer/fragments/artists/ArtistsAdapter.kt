package gabyshev.denis.musicplayer.fragments.artists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.data.Artist

/**
 * Created by 1 on 18.07.2017.
 */
class ArtistsAdapter(private val context: Context, private val arrayArtists: ArrayList<Artist>): RecyclerView.Adapter<ArtistsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ArtistsHolder {
        return ArtistsHolder(LayoutInflater.from(context).inflate(R.layout.fragment_artists_item, parent, false))
    }

    override fun getItemCount(): Int = arrayArtists.size


    override fun onBindViewHolder(holder: ArtistsHolder, position: Int) {
        holder.setHolder(context, arrayArtists[position], position)
    }
}