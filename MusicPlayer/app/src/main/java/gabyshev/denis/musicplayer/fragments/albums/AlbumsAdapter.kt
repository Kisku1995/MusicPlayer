package gabyshev.denis.musicplayer.fragments.albums

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Album

/**
 * Created by Borya on 17.07.2017.
 */
class AlbumsAdapter(private val context: Context, private val albumsArray: ArrayList<Album>): RecyclerView.Adapter<AlbumHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlbumHolder {
        return AlbumHolder(LayoutInflater.from(context).inflate(R.layout.fragment_albums_item, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.setHolder(context, albumsArray[position], position)

        holder.view.setOnClickListener {
            TracksHelper.instance().startCategory(context,
                    Category(
                            albumsArray[position].id,
                            0,
                            albumsArray[position].album
                    ))
        }
    }

    override fun getItemCount(): Int = albumsArray.size
}