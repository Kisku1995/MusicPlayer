package gabyshev.denis.musicplayer.fragments.tracks

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
 * Created by Borya on 15.07.2017.
 */
class TracksFragment : Fragment() {
    private val TAG = "TracksFragment"
    private lateinit var mRecyclerView: RecyclerView

    @Inject lateinit var rxBus: RxBus
    private var subsriptions = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context.applicationContext as App).component.inject(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val array = TracksHelper.instance().scanMP3Files(context)

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = TracksAdapter(context, array, rxBus, subsriptions)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_recycler_view, container, false)

        mRecyclerView = view.find(R.id.recyclerView)

        return view
    }

    override fun onDestroy() {
        subsriptions.dispose()
        super.onDestroy()
    }
}