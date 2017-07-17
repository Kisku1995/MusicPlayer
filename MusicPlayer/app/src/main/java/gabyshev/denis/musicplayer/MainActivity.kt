package gabyshev.denis.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import gabyshev.denis.musicplayer.fragments.PlayerViewPagerAdapter
import gabyshev.denis.musicplayer.fragments.RxMenuViewPager
import gabyshev.denis.musicplayer.fragments.menu.MenuAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var menuLayoutManager: LinearLayoutManager
    private lateinit var adapter: MenuAdapter
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        menu.layoutManager = menuLayoutManager
        adapter = MenuAdapter(this, menuLayoutManager)
        menu.adapter = adapter

        viewPager.adapter = PlayerViewPagerAdapter(supportFragmentManager);
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                Log.d(TAG, "onPageSelected : ${position}")
                adapter.setSelectedHolder(position)
            }
        }

        )


    }
}
