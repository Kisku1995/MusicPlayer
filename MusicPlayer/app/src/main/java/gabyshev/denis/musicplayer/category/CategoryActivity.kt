package gabyshev.denis.musicplayer.category

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.fragments.tracks.TracksAdapter
import gabyshev.denis.musicplayer.utils.SelectCallback
import gabyshev.denis.musicplayer.utils.SelectListener
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.utils.data.TrackData
import kotlinx.android.synthetic.main.activity_category.*
import org.jetbrains.anko.find

/**
 * Created by 1 on 18.07.2017.
 */
class CategoryActivity: AppCompatActivity(), SelectListener {
    private val TAG = "CategoryActivity"

    private var selectedArray: ArrayList<TrackData> = ArrayList<TrackData>()

    private var categoryId: Int = -1
    private var category: Int = -1 // 0 - albums, 1 - artists, 2 - genres
    private var title: String = ""

    private lateinit var callback: SelectCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        getBundle()

        (findViewById(R.id.title) as TextView).text = title
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TracksAdapter(this, TracksHelper.instance().scanForCategory(this, categoryId, category))

        back.setOnClickListener { finish() }
    }

    fun getBundle() {
        val bundle: Bundle? = intent.extras

        if(bundle != null) {
            categoryId = bundle.getInt("categoryId")
            category = bundle.getInt("category")
            title = bundle.getString("title")

            Log.d(TAG, "$categoryId $category $title")
        } else {
            finish()
        }
    }

    override fun startSelect() {
        Log.d(TAG, "start select")
        selectedArray.clear()
    }

    override fun addToSelect(tracks: ArrayList<TrackData>) {
        selectedArray.addAll(tracks)
    }

    override fun stopSelect(action: Int) {
        for(item in selectedArray) {
            Log.d(TAG, "${item.artist} : ${item.title}")
        }
    }

}