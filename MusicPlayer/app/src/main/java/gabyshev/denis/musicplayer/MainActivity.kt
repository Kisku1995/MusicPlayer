package gabyshev.denis.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gabyshev.denis.musicplayer.fragments.PlayerViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = PlayerViewPagerAdapter(supportFragmentManager);
    }
}
