package gabyshev.denis.musicplayer.fragments.genres

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.events.CategoryID
import gabyshev.denis.musicplayer.events.PlaylistID
import gabyshev.denis.musicplayer.fragments.RecyclerViewSelectAbstract
import gabyshev.denis.musicplayer.playlists.PlaylistHelper
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.Genre
import gabyshev.denis.musicplayer.utils.TrackData
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

/**
 * Created by 1 on 18.07.2017.
 */
class GenresAdapter(private val context: Context,
                    private val arrayObject: ArrayList<Genre>,
                    private val rxBus: RxBus,
                    private val subscriptions: CompositeDisposable)
    : RecyclerViewSelectAbstract<Genre, GenreHolder>(context, arrayObject, rxBus, subscriptions) {
    override val classToken = Genre::class.java

    init {
        subscribe()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenreHolder {
        return GenreHolder(LayoutInflater.from(context).inflate(R.layout.fragment_genre_item, parent, false))
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) {
        holder.setHolder(context, arrayObject[position], position)

        holderAlbumArtistGenre(holder, position, arrayObject[position].name)

        backgroundControl(holder, position)
    }

    override fun getItemCount(): Int = arrayObject.size

}