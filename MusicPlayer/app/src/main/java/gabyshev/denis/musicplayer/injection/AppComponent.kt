package gabyshev.denis.musicplayer.injection

import dagger.Component
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.fragments.player.PlayerFragment
import gabyshev.denis.musicplayer.fragments.tracks.TracksAdapter
import gabyshev.denis.musicplayer.service.MediaPlayerService
import javax.inject.Singleton

/**
 * Created by 1 on 18.07.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: App)
    fun inject(playerFragment: PlayerFragment)
    fun inject(mediaPlayerService: MediaPlayerService)
}