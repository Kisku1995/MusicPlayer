package gabyshev.denis.musicplayer.playlists

import android.content.Context
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.playlists.interfaces.OnStartDragListener
import gabyshev.denis.musicplayer.utils.TrackData
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.fragments.PlayTrack
import gabyshev.denis.musicplayer.playlists.interfaces.ItemTouchHelperAdapter
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import gabyshev.denis.musicplayer.utils.RxBus
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.doAsync
import java.util.*
import javax.inject.Inject


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

    @Inject lateinit var musicPlayer: MusicMediaPlayer

    init {
        (context.applicationContext as App).component.inject(this)
        val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(this)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onItemMove(oldPos: Int, newPos: Int): Boolean {
        moveItem(oldPos, newPos)
        return true
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
            PlayTrack.playTrack(context, arrayTracks, musicPlayer, position)
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
        doAsync {
            Collections.swap(arrayTracks, oldPos, newPos)
            MediaStore.Audio.Playlists.Members.moveItem(context.contentResolver, playlistId, oldPos, newPos);
        }
        notifyItemMoved(oldPos, newPos)
    }






}