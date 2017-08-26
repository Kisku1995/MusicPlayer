package gabyshev.denis.musicplayer.player

import android.content.Intent
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
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.fragment_player.*
import javax.inject.Inject
import gabyshev.denis.musicplayer.player.PlayerFragment.Actions.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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
            Blurry.with(context).from(bitmap).into(background)
        } else {
            image.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.no_music))
            background.setImageDrawable(null)
        }

        setPlayPauseButton()
    }

    fun setPlayPauseButton() {
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
        playPause.setOnClickListener { makeAction(RESUME) }
        previous.setOnClickListener { makeAction(PREVIOUS) }
        next.setOnClickListener { makeAction(NEXT) }
    }



    fun makeAction(action: Actions) {
        if(!MediaPlayerService.isRunning(context, MediaPlayerService::class.java)) {
            Observable.just(startService(action))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observerButtonAction)
        } else {
            buttonAction(action)
        }
    }

    fun startService(action: Actions) : Actions {
        context.startService(Intent(context, MediaPlayerService::class.java))
        return action
    }

    val observerButtonAction = object : Observer<Actions> {
        override fun onNext(t: Actions) {
            if(t == RESUME) buttonAction(PLAY)
                else buttonAction(t)
        }

        override fun onComplete() {

        }

        override fun onError(e: Throwable) {

        }

        override fun onSubscribe(d: Disposable) {

        }
    }

    fun buttonAction(action: Actions) {
        when(action) {
            RESUME -> clickPlayPauseButton()
            PREVIOUS -> musicPlayer.previousTrack()
            NEXT ->  musicPlayer.nextTrack()
            PLAY -> musicPlayer.playTrack()
        }
    }

    enum class Actions {
        RESUME,
        PREVIOUS,
        NEXT,
        PLAY
    }
}

