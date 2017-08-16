package gabyshev.denis.musicplayer.fragments

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.category.Category
import gabyshev.denis.musicplayer.events.CategoryID
import gabyshev.denis.musicplayer.events.EnumSelectStatus
import gabyshev.denis.musicplayer.events.PlaylistID
import gabyshev.denis.musicplayer.fragments.select.SelectListener
import gabyshev.denis.musicplayer.playlists.PlaylistHelper
import gabyshev.denis.musicplayer.playlists.add_tracks.AddTracksToPlaylistDialog
import gabyshev.denis.musicplayer.utils.*
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by 1 on 19.07.2017.
 */
abstract class RecyclerViewSelectAbstract<T : Identifier, K : RecyclerView.ViewHolder?>(
                               private val context: Context,
                               private val arrayObject: ArrayList<T>,
                               private val rxBus: RxBus,
                               private val subscriptions: CompositeDisposable) : RecyclerView.Adapter<K>() {

    private val TAG = "abstract"

    protected abstract val classToken: Class<T>

    var selectedObject = ArrayList<T>()
    var selectListener: SelectListener = context as SelectListener

    init {
        RxSelectListener()
    }

    fun subscribe() {
        subscriptions.add(
                rxBus.toObservable()
                        .subscribe {
                            if(it is PlaylistID) {
                                addCategoryTracksToPlaylist(it.id)
                            }
                        }
        )
    }

    fun addCategoryTracksToPlaylist(id: Long) {
        if(selectedObject.size > 0) {
            when(classToken) {
                TrackData::class.java ->  {
                    PlaylistHelper.instance().addTracksToPlaylist(id, selectedObject as ArrayList<TrackData>, context)
                }
                else -> {
                    selectedObject
                            .map { TracksHelper.instance().scanForCategory(context, it.id, CategoryID.values()[CategoryID.getType(classToken)]) }
                            .forEach { PlaylistHelper.instance().addTracksToPlaylist(id, it, context) }
                }
            }

            selectListener.stopSelect()
            selectedObject.clear()
            notifyDataSetChanged()
        }
    }

    fun RxSelectListener() {
        subscriptions.addAll(
                rxBus.toObservable()
                        .subscribe({
                            if(it is EnumSelectStatus && it == EnumSelectStatus.CANCEL) {
                                cancelSelecting()
                            }
                        }),
                rxBus.toObservable()
                        .subscribe({
                            if(it is EnumSelectStatus && it == EnumSelectStatus.ADD) {
                                addSelecting()
                            }
                        })
        )
    }

    fun checkHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isContainsItem(position)) {
            TracksHelper.instance().setBackground(context, holder.itemView, position)
        } else {
            TracksHelper.instance().setSelectedBackground(context, holder.itemView, position)
        }
        if(selectedObject.size >= 1) selectListener.countSelect((selectedObject.size).toString())
    }

    fun cancelSelecting() {
        selectedObject.clear()
        selectListener.stopSelect()
        notifyDataSetChanged()
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): K

    abstract override fun onBindViewHolder(holder: K, position: Int)

    fun holderAlbumArtistGenre(holder: K, position: Int, categoryName: String) {
        holder?.itemView?.setOnClickListener {
            if(selectedObject.size == 0) {
                TracksHelper.instance().startCategory(
                        context,
                        Category(arrayObject[position].id,
                                CategoryID.getType(classToken),
                                categoryName)
                )
            } else {
                checkHolder(holder, position)
            }
        }

        itemLongClick(holder, position)
    }

    fun holderTracks(holder: K, position: Int, operation: (position: Int) -> Unit) {
        holder?.itemView?.setOnClickListener {
            if(selectedObject.size == 0) operation(position)
            else {
                checkHolder(holder, position)
            }

        }

        holder?.itemView?.setOnLongClickListener{
            if(selectedObject.size == 0) {
                selectListener.startSelect()
            }

            checkHolder(holder, position)
            true
        }
    }

    fun itemLongClick(holder: K, position: Int) {
        holder?.itemView?.setOnLongClickListener {
            if(selectedObject.size == 0) {
                selectListener.startSelect()
            }

            checkHolder(holder, position)
            true
        }
    }

    override fun getItemCount(): Int = arrayObject.size

    fun isContainsItem(position: Int): Boolean {
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

    fun addSelecting() {
        if(selectedObject.size > 0)
            AddTracksToPlaylistDialog().show((context as AppCompatActivity).supportFragmentManager, "genre")
    }
}


