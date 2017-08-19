package gabyshev.denis.musicplayer.fragments.player

import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatDrawableManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import kotlinx.android.synthetic.main.fragment_player.*
import javax.inject.Inject

/**
 * Created by 1 on 17.07.2017.
 */
class PlayerFragment: Fragment() {
    private val TAG = "PlayerFragment"

    @Inject lateinit var musicPlayer: MusicMediaPlayer

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context.applicationContext as App).component.inject(this)

        playPause.setOnClickListener { clickPlayPauseButton() }

        isMusicPlayerPlaying()

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View
        = LayoutInflater.from(context).inflate(R.layout.fragment_player, container, false)


    fun setPlayer() {
        val track = musicPlayer.getCurrentTrack()

        setTitleArtist(track?.title, track?.artist)

        val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, track!!.albumId))
        if (bitmap != null) image.setImageBitmap(TracksHelper.instance().getRoundedShape(bitmap)) else {
            setNoAlbum()
        }

        when(musicPlayer.isPlaying()) {
            true -> setPlayPauseDrawable(R.drawable.pause)
            false -> setPlayPauseDrawable(R.drawable.play)
        }
    }

    fun destroyPlayer() {
        setTitleArtist(getString(R.string.no_title), getString(R.string.no_artist))
        setNoAlbum()
        setPlayPauseDrawable(R.drawable.play)
    }

    fun setPlayPauseDrawable(drawable: Int) {
        playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, drawable))
    }

    fun setNoAlbum() {
        image.setImageBitmap(TracksHelper.instance().getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
    }

    fun setTitleArtist(_title: String?, _artist: String?) {
        title.text = _title
        artist.text = _artist
    }

    fun clickPlayPauseButton() {
        if(musicPlayer.isPlaying()) {
            musicPlayer.pauseTrack()
        } else {
            musicPlayer.resumeTrack()
        }
    }

    fun isMusicPlayerPlaying() {
        if(MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
            setPlayer()
        } else {
            setNoAlbum()
        }
    }





}