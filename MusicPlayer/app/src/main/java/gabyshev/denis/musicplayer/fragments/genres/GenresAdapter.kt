package gabyshev.denis.musicplayer.fragments.genres

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.data.Genre

/**
 * Created by 1 on 18.07.2017.
 */
class GenresAdapter(private val context: Context, private val arrayGenres: ArrayList<Genre>): RecyclerView.Adapter<GenreHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenreHolder {
        return GenreHolder(LayoutInflater.from(context).inflate(R.layout.fragment_genre_item, parent, false))
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.setHolder(context, arrayGenres[position], position)
    }

    override fun getItemCount(): Int = arrayGenres.size

}