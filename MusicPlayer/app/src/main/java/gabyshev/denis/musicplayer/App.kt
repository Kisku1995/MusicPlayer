package gabyshev.denis.musicplayer

import android.app.Application
import gabyshev.denis.musicplayer.injection.AppComponent
import gabyshev.denis.musicplayer.injection.AppModule
import gabyshev.denis.musicplayer.injection.DaggerAppComponent

/**
 * Created by Borya on 15.07.2017.
 */

class App : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }


}
