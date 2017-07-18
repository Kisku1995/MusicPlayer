package gabyshev.denis.musicplayer.category

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.fragments.select.SelectFragment
import gabyshev.denis.musicplayer.fragments.select.SelectListener
import gabyshev.denis.musicplayer.fragments.tracks.TracksAdapter
import gabyshev.denis.musicplayer.utils.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_category.*
import javax.inject.Inject

/**
 * Created by 1 on 18.07.2017.
 */
class CategoryActivity: AppCompatActivity(), SelectListener {

    private val TAG = "CategoryActivity"

    private var categoryId: Int = -1
    private var category: Int = -1 // 0 - albums, 1 - artists, 2 - genres
    private var title: String = ""

    @Inject lateinit var rxBus: RxBus
    private var subscriptions = CompositeDisposable()

    private val selectFragment = SelectFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        app.component.inject(this)

        getBundle()

        (findViewById(R.id.title) as TextView).text = title
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TracksAdapter(this, TracksHelper.instance().scanForCategory(this, categoryId, category), rxBus, subscriptions)

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
        supportFragmentManager.beginTransaction().replace(R.id.selectFragment, selectFragment).commit()
    }

    override fun stopSelect() {
        Log.d(TAG, "stop select")
        supportFragmentManager.beginTransaction().remove(selectFragment).commit()
    }

    override fun countSelect(count: String) {
        selectFragment.selectCount(count)
    }

    override fun onDestroy() {
        subscriptions.dispose()
        super.onDestroy()
    }

}