package gabyshev.denis.musicplayer.fragments.albums

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.fragments.RecyclerViewSelectAbstract
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Album
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

/**
 * Created by Borya on 17.07.2017.
 */
class AlbumsAdapter(private val context: Context, private val arrayObject: ArrayList<Album>, private val rxBus: RxBus, private val subscriptions: CompositeDisposable)
    : RecyclerViewSelectAbstract<Album, AlbumHolder>(context, arrayObject, rxBus, subscriptions) {

    private val TAG = "AlbumsAdapter"


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

    override fun addSelecting() {
        selectListener.stopSelect()
        notifyDataSetChanged()
        for(item in selectedObject) {
            Log.d(TAG, "${item.artist} : ${item.album}")
        }
        selectedObject.clear()
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