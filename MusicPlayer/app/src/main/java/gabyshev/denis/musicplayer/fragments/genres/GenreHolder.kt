package gabyshev.denis.musicplayer.fragments.genres

import android.content.Context
import android.support.v7.widget.AppCompatDrawableManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Genre
import org.jetbrains.anko.find

/**
 * Created by 1 on 18.07.2017.
 */
class GenreHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.find(R.id.genre)

    fun setHolder(context: Context, genre: Genre, position: Int) {
        this.name.text = genre.name
        TracksHelper.instance().setBackground(context, view, position)
    }


}