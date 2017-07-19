package gabyshev.denis.musicplayer.fragments.genres

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by 1 on 18.07.2017.
 */
class GenresFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView


    @Inject lateinit var rxBus: RxBus
    private var subsriptions = CompositeDisposable()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (context.applicationContext as App).component.inject(this)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = GenresAdapter(context, TracksHelper.instance().scanForGenres(context), rxBus, subsriptions)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_recycler_view, container, false)

        recyclerView = view.find(R.id.recyclerView)

        return view
    }

    override fun onDestroy() {
        subsriptions.dispose()
        super.onDestroy()
    }
}