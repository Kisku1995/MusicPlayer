package gabyshev.denis.musicplayer.fragments.albums

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.content.Context
import android.view.LayoutInflater
import gabyshev.denis.musicplayer.R

/**
 * Created by Borya on 17.07.2017.
 */
class AlbumsAdapter(private val context: Context, private val albumsArray: ArrayList<Album>): RecyclerView.Adapter<AlbumsHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlbumsHolder {
        return AlbumsHolder(LayoutInflater.from(context).inflate(R.layout.fragment_albums_item, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumsHolder, position: Int) {
        holder.setHolder(context, albumsArray[position], position)
    }

    override fun getItemCount(): Int = albumsArray.size
}