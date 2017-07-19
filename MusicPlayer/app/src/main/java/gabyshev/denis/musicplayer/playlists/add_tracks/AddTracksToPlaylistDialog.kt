package gabyshev.denis.musicplayer.playlists.add_tracks

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumSelectStatus
import gabyshev.denis.musicplayer.events.RVAdapterStatus
import gabyshev.denis.musicplayer.events.SelectTrackStatus
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by 1 on 19.07.2017.
 */
class AddTracksToPlaylistDialog : DialogFragment() {
    private lateinit var recyclerView: RecyclerView

    @Inject lateinit var rxBus: RxBus

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context.applicationContext as App).component.inject(this)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = AddTracksAdapter(context, TracksHelper.instance().scanForPlaylists(context), rxBus, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_add_tracks_to_playlist, container, false)

        recyclerView = view.find(R.id.recyclerView)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        rxBus.send(SelectTrackStatus(EnumSelectStatus.CANCEL))
    }


}