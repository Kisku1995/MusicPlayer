package gabyshev.denis.musicplayer.fragments.player

import android.app.Fragment
import android.graphics.*
import android.os.Bundle
import android.support.v7.widget.AppCompatDrawableManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.service.activityplayer.RxServiceActivity
import gabyshev.denis.musicplayer.utils.TracksHelper
import gabyshev.denis.musicplayer.service.MediaPlayerService
import gabyshev.denis.musicplayer.service.activityplayer.ServiceActivity
import org.jetbrains.anko.find

/**
 * Created by 1 on 17.07.2017.
 */
class PlayerFragment: Fragment() {
    private val TAG = "PlayerFragment"

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var artist: TextView
    private lateinit var playPause: ImageView

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
        image.setImageBitmap(TracksHelper.instance().getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
        RxListener()

        if(RxServiceActivity.instance()?.track != null) {
            Log.d(TAG, RxServiceActivity.instance()?.track?.track?.title)
            setPlayer(RxServiceActivity.instance()?.track!!)
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
                RxServiceActivity.instance()?.setActivityService(0)
            } else {
                RxServiceActivity.instance()?.setActivityService(1)
            }
        }

        RxServiceActivity.instance()?.getServiceActivity()?.subscribe({
            setPlayer(it)
        })
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
            0 -> {
                title.text = getString(R.string.no_title)
                artist.text = getString(R.string.no_artist)
                image.setImageBitmap(TracksHelper.instance().getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
                playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, R.drawable.play))
                isPlaying = false
                playPause.isEnabled = false
            }
            1 -> {
                playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, R.drawable.pause))
                isPlaying = true
                playPause.isEnabled = true
            }
            2 -> {
                playPause.setImageDrawable(AppCompatDrawableManager.get().getDrawable(context, R.drawable.play))
                isPlaying = false
            }
        }

    }

    override fun onDestroy() {
        RxServiceActivity.instance()?.getServiceActivity()?.onComplete()
        RxServiceActivity.instance()?.createServiceActivity()
        super.onDestroy()
    }

}