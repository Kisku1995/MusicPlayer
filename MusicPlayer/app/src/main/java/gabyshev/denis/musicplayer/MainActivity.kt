package gabyshev.denis.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import gabyshev.denis.musicplayer.fragments.PlayerViewPagerAdapter
import gabyshev.denis.musicplayer.fragments.ZoomOutPageTransformer
import gabyshev.denis.musicplayer.fragments.player.PlayerFragment
import gabyshev.denis.musicplayer.fragments.select.SelectFragment
import gabyshev.denis.musicplayer.fragments.select.SelectListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SelectListener {
    private val TAG = "MainActivity"

    private val player = PlayerFragment.instance()
    private var selectFragment = SelectFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = PlayerViewPagerAdapter(supportFragmentManager)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
        menu.setupWithViewPager(viewPager)

        supportFragmentManager.beginTransaction().replace(R.id.player, player).commit()
    }

    override fun startSelect() {
       supportFragmentManager.beginTransaction().add(R.id.player, selectFragment).commit()
    }

    override fun stopSelect() {
        supportFragmentManager.beginTransaction().remove(selectFragment).commit()
    }

    override fun countSelect(count: String) {
        try {
            selectFragment.selectCount(count)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
