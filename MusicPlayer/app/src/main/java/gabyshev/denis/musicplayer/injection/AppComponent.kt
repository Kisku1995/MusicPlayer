package gabyshev.denis.musicplayer.injection

import dagger.Component
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.main.MainActivity
import gabyshev.denis.musicplayer.category.CategoryActivity
import gabyshev.denis.musicplayer.fragments.albums.AlbumsFragment
import gabyshev.denis.musicplayer.fragments.artists.ArtistsFragment
import gabyshev.denis.musicplayer.fragments.genres.GenresFragment
import gabyshev.denis.musicplayer.fragments.player.MainPlayerFragment
import gabyshev.denis.musicplayer.fragments.playlists.PlaylistsFragment
import gabyshev.denis.musicplayer.fragments.select.SelectFragment
import gabyshev.denis.musicplayer.fragments.tracks.TracksAdapter
import gabyshev.denis.musicplayer.fragments.tracks.TracksFragment
import gabyshev.denis.musicplayer.main.MainFragment
import gabyshev.denis.musicplayer.player.PlayerActivity
import gabyshev.denis.musicplayer.player.PlayerFragment
import gabyshev.denis.musicplayer.playlists.AddPlaylistDialog
import gabyshev.denis.musicplayer.playlists.DeletePlaylistDialog
import gabyshev.denis.musicplayer.playlists.PlaylistActivity
import gabyshev.denis.musicplayer.playlists.PlaylistAdapter
import gabyshev.denis.musicplayer.playlists.add_tracks.AddTracksToPlaylistDialog
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import javax.inject.Singleton

/**
 * Created by 1 on 18.07.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: App)
    fun inject(playerFragment: MainPlayerFragment)
    fun inject(mediaPlayerService: MediaPlayerService)
    fun inject(categoryActivity: CategoryActivity)
    fun inject(tracksFragment: TracksFragment)
    fun inject(selectFragment: SelectFragment)
    fun inject(albumsFragment: AlbumsFragment)
    fun inject(artistsFragment: ArtistsFragment)
    fun inject(genresFragment: GenresFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(addPlaylistDialog: AddPlaylistDialog)
    fun inject(playlistsFragment: PlaylistsFragment)
    fun inject(addTracksToPlaylistDialog: AddTracksToPlaylistDialog)
    fun inject(deletePlaylistDialog: DeletePlaylistDialog)
    fun inject(playlistActivity: PlaylistActivity)
    fun inject(musicMediaPlayer: MusicMediaPlayer)
    fun inject(tracksAdapter: TracksAdapter)
    fun inject(playlistAdapter: PlaylistAdapter)
    fun inject(playerFragment: PlayerFragment)
    fun inject(playerActivity: PlayerActivity)
    fun inject(mainFragment: MainFragment)
}