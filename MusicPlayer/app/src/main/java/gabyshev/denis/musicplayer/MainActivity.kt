package gabyshev.denis.musicplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import gabyshev.denis.musicplayer.events.EnumSelectStatus
import gabyshev.denis.musicplayer.fragments.PlayerViewPagerAdapter
import gabyshev.denis.musicplayer.fragments.ZoomOutPageTransformer
import gabyshev.denis.musicplayer.fragments.player.PlayerFragment
import gabyshev.denis.musicplayer.fragments.select.SelectFragment
import gabyshev.denis.musicplayer.fragments.select.SelectListener
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.app
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SelectListener {
    private val TAG = "MainActivity"

    private val player = PlayerFragment.instance()
    private var selectFragment = SelectFragment()

    private var isSelect: Boolean = false

    @Inject lateinit var rxBus: RxBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app.component.inject(this)

        viewPager.adapter = PlayerViewPagerAdapter(supportFragmentManager)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
        menu.setupWithViewPager(viewPager)

        supportFragmentManager.beginTransaction().replace(R.id.player, player).commit()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if(isSelect) rxBus.send(EnumSelectStatus.CANCEL)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun startSelect() {
       supportFragmentManager.beginTransaction().add(R.id.player, selectFragment).commit()
        isSelect = true
    }

    override fun stopSelect() {
        supportFragmentManager.beginTransaction().remove(selectFragment).commit()
        isSelect = false
    }

    override fun countSelect(count: String) {
        try {
            selectFragment.selectCount(count)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
