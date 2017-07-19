package gabyshev.denis.musicplayer.playlists

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.app
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_category.*
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by 1 on 19.07.2017.
 */
class PlaylistActivity : AppCompatActivity() {

    @Inject lateinit var rxBus: RxBus
    private var subsriptions = CompositeDisposable()


    private val TAG = " PlaylistActivity"

    private var id: Long = -1L
    private var bundleTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        app.component.inject(this)

        getBundle()

        back.setOnClickListener {
            finish()
        }

        (findViewById(R.id.title) as TextView).text = bundleTitle

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistAdapter(this, PlaylistHelper.instance().getPlaylistTracks(this, id), recyclerView, id, rxBus, subsriptions)

        (findViewById(R.id.delete) as ImageView).setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("id", id)
            bundle.putString("title", bundleTitle)

            val deleteDialog = DeletePlaylistDialog()
            deleteDialog.arguments = bundle
            deleteDialog.show(supportFragmentManager, "deletePlaylist")



        }
    }

    fun getBundle() {
        val bundle: Bundle? = intent.extras

        if(bundle != null) {
            id = bundle.getLong("id")
            bundleTitle = bundle.getString("title")

            Log.d(TAG, "$id $bundleTitle")
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subsriptions.dispose()
    }
}