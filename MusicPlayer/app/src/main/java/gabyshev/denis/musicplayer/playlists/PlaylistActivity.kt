package gabyshev.denis.musicplayer.playlists

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import kotlinx.android.synthetic.main.activity_category.*
import org.jetbrains.anko.find

/**
 * Created by 1 on 19.07.2017.
 */
class PlaylistActivity : AppCompatActivity() {

    private val TAG = " PlaylistActivity"

    private var id: Long = -1L
    private var bundleTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        getBundle()

        (findViewById(R.id.title) as TextView).text = bundleTitle

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistAdapter(this, PlaylistHelper.instance().getPlaylistTracks(this, id))
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
}