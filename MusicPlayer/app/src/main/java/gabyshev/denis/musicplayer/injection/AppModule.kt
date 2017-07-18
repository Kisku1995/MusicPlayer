package gabyshev.denis.musicplayer.injection

import dagger.Module
import dagger.Provides
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.utils.RxBus
import javax.inject.Singleton

/**
 * Created by 1 on 18.07.2017.
 */
@Module class AppModule(val app: App) {
    @Provides @Singleton fun provideApp() = app

    @Provides @Singleton fun provideBus() = RxBus()
}