package gabyshev.denis.musicplayer.player

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatDrawableManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.mediaplayer.MusicMediaPlayer
import gabyshev.denis.musicplayer.utils.TracksHelper
import kotlinx.android.synthetic.main.fragment_player.*
import javax.inject.Inject

/**
 * Created by Borya on 24.08.2017.
 */
class PlayerFragment : Fragment() {
    @Inject lateinit var musicPlayer: MusicMediaPlayer

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context.applicationContext as App).component.inject(this)

        isMusicPlayerPlaying()

        setButtonControl()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_player, container, false)
    }

    fun isMusicPlayerPlaying() {
        if(MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
            setPlayer()
        }
    }

    fun setPlayer() {
        val track = musicPlayer.getCurrentTrack()

        title.text = track?.title
        artist.text = track?.artist

        val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, track!!.albumId))

        if(bitmap != null) {
            image.setImageBitmap(bitmap)
        } else {
            image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_music))
        }

        when(musicPlayer.isPlaying()) {
            true -> setPlayPauseDrawable(R.drawable.pause)
            false -> setPlayPauseDrawable(R.drawable.play)
        }
    }

    fun setPlayPauseDrawable(drawable: Int) {
        playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, drawable))
    }

    fun clickPlayPauseButton() {
        if(musicPlayer.isPlaying()) {
            musicPlayer.pauseTrack()
        } else {
            musicPlayer.resumeTrack()
        }
    }

    fun setButtonControl() {
        playPause.setOnClickListener { clickPlayPauseButton() }
        previous.setOnClickListener { musicPlayer.previousTrack() }
        next.setOnClickListener { musicPlayer.nextTrack() }
    }

}