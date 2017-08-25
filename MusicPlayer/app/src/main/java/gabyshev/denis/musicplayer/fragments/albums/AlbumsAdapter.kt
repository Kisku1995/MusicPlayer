package gabyshev.denis.musicplayer.fragments.albums

import android.view.ViewGroup
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.events.CategoryID
import gabyshev.denis.musicplayer.events.PlaylistID
import gabyshev.denis.musicplayer.fragments.RecyclerViewSelectAbstract
import gabyshev.denis.musicplayer.playlists.PlaylistHelper
import gabyshev.denis.musicplayer.utils.Album
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.TrackData
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

/**
 * Created by Borya on 17.07.2017.
 */
class AlbumsAdapter(private val context: Context,
                    private val arrayObject: ArrayList<Album>,
                    private val rxBus: RxBus,
                    private val subscriptions: CompositeDisposable)
    : RecyclerViewSelectAbstract<Album, AlbumHolder>(context,
                                                    arrayObject,
                                                    rxBus,
                                                    subscriptions) {
    override val classToken = Album::class.java

    init {
        subscribe()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlbumHolder {
        return AlbumHolder(LayoutInflater.from(context).inflate(R.layout.fragment_albums_item, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        holder.setHolder(context, arrayObject[position], position)

        holderAlbumArtistGenre(holder, position, arrayObject[position].album)

        backgroundControl(holder, position)
    }

}