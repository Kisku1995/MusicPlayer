package gabyshev.denis.musicplayer.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumSelectStatus
import gabyshev.denis.musicplayer.fragments.PlayerViewPagerAdapter
import gabyshev.denis.musicplayer.fragments.ZoomOutPageTransformer
import gabyshev.denis.musicplayer.fragments.player.MainPlayerFragment
import gabyshev.denis.musicplayer.fragments.select.SelectFragment
import gabyshev.denis.musicplayer.fragments.select.SelectListener
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.app
import gabyshev.denis.musicplayer.utils.transparentStatusBar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), SelectListener {
    val mainFragment = MainFragment()

    override fun startSelect() {
        mainFragment.startSelect()
    }

    override fun stopSelect() {
        mainFragment.stopSelect()
    }

    override fun countSelect(count: String) {
        mainFragment.countSelect(count)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
    }


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(android.Manifest.permission.MEDIA_CONTENT_CONTROL) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL), 1337)
            } else {
                setupFragment()
            }
        } else {
            setupFragment()
        }
    }

    private fun checkPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(android.Manifest.permission.MEDIA_CONTENT_CONTROL) == PackageManager.PERMISSION_GRANTED)
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        setupFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setupFragment()
    }

    private fun setupFragment() {
        if(checkPermissionGranted())
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, mainFragment).commitAllowingStateLoss()
        else
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, PermissionFragment()).commitAllowingStateLoss()


    }
}
