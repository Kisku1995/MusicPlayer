package gabyshev.denis.musicplayer.fragments.albums

import android.content.Context
import android.net.Uri
import android.support.v7.widget.AppCompatDrawableManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.data.Album
import gabyshev.denis.musicplayer.utils.CircleTransform
import gabyshev.denis.musicplayer.utils.TracksHelper
import org.jetbrains.anko.find
import java.io.File

/**
 * Created by Borya on 17.07.2017.
 */

class AlbumHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val cover: ImageView = view.find(R.id.cover)
    private val album: TextView = view.find(R.id.album)
    private val artist: TextView = view.find(R.id.artist)

    fun setHolder(context: Context, album: Album, position: Int) {
        this.album.text = album.album
        artist.text = album.artist

        Picasso.with(context)
                .load(Uri.fromFile(File(album.cover)))
                .transform(CircleTransform())
                .resize(96, 96)
                .centerCrop()
                .into(cover)

        TracksHelper.instance().setBackground(context, view, position)
    }


}
