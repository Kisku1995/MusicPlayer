package gabyshev.denis.musicplayer.utils

import android.app.Activity
import gabyshev.denis.musicplayer.App

/**
 * Created by 1 on 18.07.2017.
 */
val Activity.app: App
    get() = application as App
