package gabyshev.denis.musicplayer.fragments.playlists

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.playlists.AddPlaylistDialog
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by 1 on 17.07.2017.
 */
class PlaylistsFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton

    @Inject lateinit var rxBus: RxBus
    private var subsriptions = CompositeDisposable()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context.applicationContext as App).component.inject(this)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = PlaylistsAdapter(context, TracksHelper.instance().scanForPlaylists(context), rxBus, subsriptions)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(dy > 0) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }
        })

        fab.setOnClickListener {
            AddPlaylistDialog().show(fragmentManager, "addPlaylist")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_playlist, container, false)

        recyclerView = view.find(R.id.recyclerView)
        fab = view.find(R.id.fab)

        return view
    }


    override fun onDestroy() {
        subsriptions.dispose()
        super.onDestroy()
    }
}