package gabyshev.denis.musicplayer.player

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.readystatesoftware.systembartint.SystemBarTintManager
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.app
import javax.inject.Inject
import android.view.Window.ID_ANDROID_CONTENT



/**
 * Created by Borya on 24.08.2017.
 */
class PlayerActivity : AppCompatActivity() {
    @Inject lateinit var playerFragment: PlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        app.component.inject(this)

        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, playerFragment).commit()

        transparentStatusBar(this)


    }

    fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            activity.window.statusBarColor = Color.TRANSPARENT
        }

        val tintManager = SystemBarTintManager(activity)
        // enable status bar tint
        tintManager.isStatusBarTintEnabled = true
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true)
    }
}