package gabyshev.denis.musicplayer.fragments.artists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.fragments.RecyclerViewSelectAbstract
import gabyshev.denis.musicplayer.fragments.tracks.TracksHolder
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Artist
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

/**
 * Created by 1 on 18.07.2017.
 */
class ArtistsAdapter(private val context: Context, private val arrayObject: ArrayList<Artist>, private val rxBus: RxBus, private val subscriptions: CompositeDisposable) : RecyclerViewSelectAbstract<Artist, ArtistHolder>(context, arrayObject, rxBus, subscriptions) {
    private val TAG = "ArtistsAdapter"

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

        return false    }

    override fun addSelecting() {
        selectListener.stopSelect()
        notifyDataSetChanged()
        for(item in selectedObject) {
            Log.d(TAG, "${item.artist} : ${item.trackCount}")
        }
        selectedObject.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ArtistHolder {
        return ArtistHolder(LayoutInflater.from(context).inflate(R.layout.fragment_artists_item, parent, false))
    }

    override fun getItemCount(): Int = arrayObject.size


    override fun onBindViewHolder(holder: ArtistHolder?, position: Int) {
        holder?.setHolder(context, arrayObject[position], position)


        holder?.itemView?.setOnClickListener {
            if(selectedObject.size == 0) {
                TracksHelper.instance().startCategory(context,
                        Category(
                                arrayObject[position].id,
                                1,
                                arrayObject[position].artist
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