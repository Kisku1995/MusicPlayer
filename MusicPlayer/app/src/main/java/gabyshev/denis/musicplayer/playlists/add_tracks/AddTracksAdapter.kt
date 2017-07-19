package gabyshev.denis.musicplayer.playlists.add_tracks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumRVAdapterStatus
import gabyshev.denis.musicplayer.events.PlaylistID
import gabyshev.denis.musicplayer.events.RVAdapterStatus
import gabyshev.denis.musicplayer.fragments.playlists.PlaylistHolder
import gabyshev.denis.musicplayer.playlists.PlaylistActivity
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.Playlist
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by 1 on 19.07.2017.
 */
class AddTracksAdapter(private val context: Context, private var playlistsArray: ArrayList<Playlist>, private val rxBus: RxBus, private val fragment: DialogFragment):
        RecyclerView.Adapter<PlaylistHolder>() {

    private var TAG = "PlaylistsAdapter"

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.setHolder(context, playlistsArray[position], position)
        holder.itemView.setOnClickListener {
            rxBus.send(PlaylistID(playlistsArray[position].id.toLong()))
            fragment.dismiss()
        }
    }

    override fun getItemCount(): Int = playlistsArray.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PlaylistHolder {
        return PlaylistHolder(LayoutInflater.from(context).inflate(R.layout.fragment_playlist_item, parent, false))
    }


}