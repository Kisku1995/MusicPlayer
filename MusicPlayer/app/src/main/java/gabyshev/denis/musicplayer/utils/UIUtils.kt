package gabyshev.denis.musicplayer.utils

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import com.readystatesoftware.systembartint.SystemBarTintManager

/**
 * Created by Borya on 26.08.2017.
 */
fun transparentStatusBar(activity: Activity, condition: Boolean) {
    if (Build.VERSION.SDK_INT >= 21) {
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        activity.window.statusBarColor = Color.TRANSPARENT
    }

    val tintManager = SystemBarTintManager(activity)
    // enable status bar tint
    tintManager.isStatusBarTintEnabled = condition
    // enable navigation bar tint
    tintManager.setNavigationBarTintEnabled(condition)
}