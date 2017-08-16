package gabyshev.denis.musicplayer.playlists

import android.content.Context
import android.support.v7.widget.AppCompatDrawableManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.playlists.interfaces.ItemTouchHelperViewHolder
import gabyshev.denis.musicplayer.utils.TrackData
import org.jetbrains.anko.find

/**
 * Created by 1 on 19.07.2017.
 */
class PlaylistHolder(val context: Context, val view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
    override fun onItemSelected() {
        view.background = AppCompatDrawableManager.get().getDrawable(context, R.color.on_drag)
    }

    override fun onItemClear() {
        view.background = AppCompatDrawableManager.get().getDrawable(context, R.color.colorPrimary)
    }

    val reorder: ImageView = view.find(R.id.reorder)
    val delete: ImageView = view.find(R.id.delete)
    private val title: TextView = view.find(R.id.title)
    private val artist: TextView = view.find(R.id.artist)
    private val duration: TextView = view.find(R.id.duration)

    fun setHolder(track: TrackData) {
        title.text = track.title
        artist.text = track.artist
        duration.text = track.duration
    }

}