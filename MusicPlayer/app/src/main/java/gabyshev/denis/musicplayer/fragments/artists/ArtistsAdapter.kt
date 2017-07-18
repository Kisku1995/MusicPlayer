package gabyshev.denis.musicplayer.fragments.artists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Artist

/**
 * Created by 1 on 18.07.2017.
 */
class ArtistsAdapter(private val context: Context, private val arrayArtists: ArrayList<Artist>): RecyclerView.Adapter<ArtistHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ArtistHolder {
        return ArtistHolder(LayoutInflater.from(context).inflate(R.layout.fragment_artists_item, parent, false))
    }

    override fun getItemCount(): Int = arrayArtists.size


    override fun onBindViewHolder(holder: ArtistHolder, position: Int) {
        holder.setHolder(context, arrayArtists[position], position)

        holder.view.setOnClickListener {
            TracksHelper.instance().startCategory(context,
                    Category(
                            arrayArtists[position].id,
                            1,
                            arrayArtists[position].artist
                    ))
        }
    }
}