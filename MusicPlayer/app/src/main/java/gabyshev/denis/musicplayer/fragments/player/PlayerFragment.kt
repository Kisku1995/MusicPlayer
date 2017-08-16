package gabyshev.denis.musicplayer.fragments.player


import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatDrawableManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.events.ServiceActivity
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatus
import gabyshev.denis.musicplayer.service.mediaplayer.MediaPlayerStatusEvent
import gabyshev.denis.musicplayer.utils.RxBus
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by 1 on 17.07.2017.
 */
class PlayerFragment: Fragment() {
    private val TAG = "PlayerFragment"

   @Inject lateinit var rxBus: RxBus

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var artist: TextView
    private lateinit var playPause: ImageView

    private var subsriptions = CompositeDisposable()

    private var isPlaying = false

    companion object {
        private var instance: PlayerFragment? = null

        fun instance(): PlayerFragment? {
            if(instance == null) {
                instance = PlayerFragment()
            }
            return instance
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context.applicationContext as App).component.inject(this)

        image.setImageBitmap(TracksHelper.instance().getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
        RxListener()

        if(rxBus.track != null) {
            setPlayer(rxBus.track!!)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_player, container, false)
        image = view.find(R.id.image)
        title = view.find(R.id.title)
        artist = view.find(R.id.artist)
        playPause = view.find(R.id.playPause)
        return view
    }

    fun RxListener() {
        playPause.setOnClickListener {
            if(MediaPlayerService.isRunning(context,  MediaPlayerService::class.java) && isPlaying) {
                rxBus.send(MediaPlayerStatusEvent(MediaPlayerStatus.PAUSE.action))
            } else {
                rxBus.send(MediaPlayerStatusEvent(MediaPlayerStatus.RESUME.action))
            }
        }

        subsriptions.add(
                rxBus.toObservable()
                        .subscribe({
                            if(it is ServiceActivity) {
                                setPlayer(it)
                            }
                        })
        )
    }

    private fun setPlayer(it: ServiceActivity) {
        if(it.action >= 0) {
            title.text = it.track?.title
            artist.text = it.track?.artist

            val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, it.track!!.albumId))
            if (bitmap != null) image.setImageBitmap(TracksHelper.instance().getRoundedShape(bitmap)) else {
                image.setImageBitmap(TracksHelper.instance().getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
            }
        }

        when(it.action) {
            MediaPlayerStatus.DESTROY.action -> {
                title.text = getString(R.string.no_title)
                artist.text = getString(R.string.no_artist)
                image.setImageBitmap(TracksHelper.instance().getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
                playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, R.drawable.play))
                isPlaying = false
                playPause.isEnabled = false
            }
            MediaPlayerStatus.RESUME.action -> {
                playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, R.drawable.pause))
                isPlaying = true
                playPause.isEnabled = true
            }
            MediaPlayerStatus.PAUSE.action -> {
                playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, R.drawable.play))
                isPlaying = false
            }
        }

    }

    override fun onDestroy() {
        subsriptions.dispose()
        super.onDestroy()
    }

}