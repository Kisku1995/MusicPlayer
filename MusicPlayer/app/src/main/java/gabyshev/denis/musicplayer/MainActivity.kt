package gabyshev.denis.musicplayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.util.Log
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

        checkPermission()

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
        Log.d(TAG, "Start Select")
       supportFragmentManager.beginTransaction().add(R.id.player, selectFragment).commit()
        isSelect = true
    }

    override fun stopSelect() {
        Log.d(TAG, "Stop Select")
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

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.MEDIA_CONTENT_CONTROL) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL), 1337)
            }
        }
    }
}
