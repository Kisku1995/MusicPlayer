package gabyshev.denis.musicplayer.fragments.playlists

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumRVAdapterStatus
import gabyshev.denis.musicplayer.events.RVAdapterStatus
import gabyshev.denis.musicplayer.playlists.PlaylistActivity
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.Playlist
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by 1 on 18.07.2017.
 */
class PlaylistsAdapter(private val context: Context, private var playlistsArray: ArrayList<Playlist>, private val rxBus: RxBus, private val subsriptions : CompositeDisposable):
        RecyclerView.Adapter<PlaylistHolder>() {

    private var TAG = "PlaylistsAdapter"

    init {
        subsriptions.add(
                rxBus.toObservable()
                        .subscribe({
                            if(it is RVAdapterStatus && it.action == EnumRVAdapterStatus.UPDATE) {
                                Log.d(TAG, "UPDATE $it")
                                playlistsArray = TracksHelper.instance().scanForPlaylists(context)
                                notifyDataSetChanged()
                            }
                        })
        )
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.setHolder(context, playlistsArray[position], position)
        holder.itemView.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putLong("id", playlistsArray[position].id.toLong())
            bundle.putString("title", playlistsArray[position].name)

            val intent: Intent = Intent(context, PlaylistActivity::class.java)
            intent.putExtras(bundle)

            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = playlistsArray.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaylistHolder {
        return PlaylistHolder(LayoutInflater.from(context).inflate(R.layout.fragment_playlist_item, parent, false))
    }


}