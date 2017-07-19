package gabyshev.denis.musicplayer.fragments.albums

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.events.PlaylistID
import gabyshev.denis.musicplayer.fragments.RecyclerViewSelectAbstract
import gabyshev.denis.musicplayer.playlists.PlaylistHelper
import gabyshev.denis.musicplayer.playlists.add_tracks.AddTracksToPlaylistDialog
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Album
import gabyshev.denis.musicplayer.utils.data.TrackData
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

/**
 * Created by Borya on 17.07.2017.
 */
class AlbumsAdapter(private val context: Context, private val arrayObject: ArrayList<Album>, private val rxBus: RxBus, private val subscriptions: CompositeDisposable)
    : RecyclerViewSelectAbstract<Album, AlbumHolder>(context, arrayObject, rxBus, subscriptions) {

    private val TAG = "AlbumsAdapter"

    init {
        subscriptions.add(
                rxBus.toObservable()
                        .subscribe{
                            if(it is PlaylistID) {
                                Log.d(TAG, "playlist ID : ${it.id}")
                                if (selectedObject.size > 0) {
                                    for (item in selectedObject) {
                                        val arrayTracks: ArrayList<TrackData> = TracksHelper.instance().scanForCategory(context, item.id, 0)
                                        for (tracks in arrayTracks) {
                                            Log.d(TAG, "${tracks.artist} : ${tracks.title}")
                                        }

                                        PlaylistHelper.instance().addTracksToPlaylist(it.id, arrayTracks, context)
                                    }
                                    selectListener.stopSelect()
                                    selectedObject.clear()
                                    notifyDataSetChanged()

                                }
                            }
                        }
        )
    }


    override fun isContainsTrack(position: Int): Boolean {
        val trackId: Int = arrayObject[position].id

        for(item in selectedObject) {
            if(trackId == item.id) {
                selectedObject.remove(item)
                if(selectedObject.size == 0) {
                    selectListener.stopSelect()
                }
                return true
            }
        }

        selectedObject.add(arrayObject[position])

        return false
    }




    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AlbumHolder {
        return AlbumHolder(LayoutInflater.from(context).inflate(R.layout.fragment_albums_item, parent, false))
    }

    override fun onBindViewHolder(holder: AlbumHolder?, position: Int) {
        holder?.setHolder(context, arrayObject[position], position)

        holder?.itemView?.setOnClickListener {
            if(selectedObject.size == 0) {
                TracksHelper.instance().startCategory(context,
                        Category(
                                arrayObject[position].id,
                                0,
                                arrayObject[position].album
                        ))
            }
            else {
                checkHolder(holder, position)
            }

        }

        holder?.itemView?.setOnLongClickListener(View.OnLongClickListener {
            if(selectedObject.size == 0) {
                selectListener.startSelect()
            }

            checkHolder(holder, position)
            true
        })
    }

}