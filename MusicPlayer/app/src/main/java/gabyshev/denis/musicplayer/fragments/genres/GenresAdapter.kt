package gabyshev.denis.musicplayer.fragments.genres

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
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
import gabyshev.denis.musicplayer.playlists.add_tracks.AddTracksToPlaylistDialog
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Genre
import gabyshev.denis.musicplayer.utils.data.TrackData
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

/**
 * Created by 1 on 18.07.2017.
 */
class GenresAdapter(private val context: Context,
                    private val arrayObject: ArrayList<Genre>,
                    private val rxBus: RxBus,
                    private val subscriptions: CompositeDisposable) : RecyclerViewSelectAbstract<Genre, GenreHolder>(context, arrayObject, rxBus, subscriptions) {

    private val TAG = "GenreAdapter"

    init {
        subscriptions.add(
                rxBus.toObservable()
                        .subscribe{
                            if(it is PlaylistID) {
                                Log.d(TAG, "playlist ID : ${it.id}")
                                if (selectedObject.size > 0) {
                                    for (item in selectedObject) {
                                        val arrayTracks: ArrayList<TrackData> = TracksHelper.instance().scanForCategory(context, item.id, CategoryID.GENRES)
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



    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GenreHolder {
        return GenreHolder(LayoutInflater.from(context).inflate(R.layout.fragment_genre_item, parent, false))
    }

    override fun onBindViewHolder(holder: GenreHolder?, position: Int) {
        holder?.setHolder(context, arrayObject[position], position)



        holder?.itemView?.setOnClickListener {
            if(selectedObject.size == 0) {
                TracksHelper.instance().startCategory(context,
                        Category(
                                arrayObject[position].id,
                                CategoryID.GENRES.category,
                                arrayObject[position].name
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

    override fun getItemCount(): Int = arrayObject.size

}