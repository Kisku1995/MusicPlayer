package gabyshev.denis.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gabyshev.denis.musicplayer.fragments.PlayerViewPagerAdapter
import gabyshev.denis.musicplayer.fragments.ZoomOutPageTransformer
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.SelectListener
import gabyshev.denis.musicplayer.utils.app
import gabyshev.denis.musicplayer.utils.data.TrackData
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SelectListener {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = PlayerViewPagerAdapter(supportFragmentManager);
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
        menu.setupWithViewPager(viewPager)
    }

    override fun startSelect() {
    }

    override fun addToSelect(tracks: ArrayList<TrackData>) {
    }

    override fun stopSelect(action: Int) {
    }

}
