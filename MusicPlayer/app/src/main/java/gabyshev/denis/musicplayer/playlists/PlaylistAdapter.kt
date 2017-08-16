package gabyshev.denis.musicplayer.playlists

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.playlists.interfaces.OnStartDragListener
import gabyshev.denis.musicplayer.utils.TrackData
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import gabyshev.denis.musicplayer.events.MediaPlayerStatusEvent
import gabyshev.denis.musicplayer.events.TrackPosition
import gabyshev.denis.musicplayer.events.TracksArray
import gabyshev.denis.musicplayer.playlists.interfaces.ItemTouchHelperAdapter
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.utils.RxBus
import io.reactivex.disposables.CompositeDisposable
import java.util.*


/**
 * Created by 1 on 19.07.2017.
 */
class PlaylistAdapter(private val context: Context,
                      private var arrayTracks: ArrayList<TrackData>,
                      recyclerView: RecyclerView,
                      val playlistId: Long,
                      val rxBus: RxBus,
                      val subscriptions: CompositeDisposable
): RecyclerView.Adapter<PlaylistHolder>(), ItemTouchHelperAdapter, OnStartDragListener {

    private val TAG = "PlaylistAdapter"

    private var itemTouchHelper: ItemTouchHelper
    private var onStartDragListener: OnStartDragListener = this

    init {
        val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(this)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onItemMove(oldPos: Int, newPos: Int): Boolean {
        moveItem(oldPos, newPos)
        return false
    }

    override fun onItemSwipe(pos: Int) {
        arrayTracks.removeAt(pos)
        PlaylistHelper.instance().deleteTrackFromPlaylist(context, playlistId, pos.toLong())
        notifyItemRemoved(pos)

    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun getItemCount(): Int = arrayTracks.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaylistHolder {
        return PlaylistHolder(context, LayoutInflater.from(context).inflate(R.layout.activity_playlist_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.setHolder(arrayTracks[position])

        holder.view.setOnClickListener {
            playTrack(position)
        }

        holder.reorder.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                onStartDragListener.onStartDrag(holder)
            }
            false
        })

        holder.delete.setOnClickListener {
            onItemSwipe(holder.adapterPosition)
        }
    }

    private fun moveItem(oldPos: Int, newPos: Int) {
        Collections.swap(arrayTracks, oldPos, newPos)
        MediaStore.Audio.Playlists.Members.moveItem(context.contentResolver, playlistId, oldPos, newPos);
        notifyItemMoved(oldPos, newPos)
    }

    private fun playTrack(position: Int) {
        if(!MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
            Log.d(TAG, "service not running")

            context.startService(Intent(context, MediaPlayerService::class.java))

            subscriptions.add(
                    rxBus.toObservable()
                            .subscribe({
                                if(it is MediaPlayerStatusEvent && it.action == MediaPlayerStatus.CREATE.action) {
                                    rxBus.send(TracksArray(arrayTracks))
                                    rxBus.send(TrackPosition(position))
                                }
                            })
            )
        } else {
            Log.d(TAG, "service running")
            rxBus.send(TracksArray(arrayTracks))
            rxBus.send(TrackPosition(position))
        }
    }





}