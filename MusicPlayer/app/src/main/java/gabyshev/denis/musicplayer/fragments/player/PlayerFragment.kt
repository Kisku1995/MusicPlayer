package gabyshev.denis.musicplayer.fragments.player

import android.app.Fragment
import android.graphics.*
import android.os.Bundle
import android.support.v7.widget.AppCompatDrawableManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.service.activityplayer.RxServiceActivity
import gabyshev.denis.musicplayer.fragments.tracks.TracksHelper
import gabyshev.denis.musicplayer.service.MediaPlayerService
import org.jetbrains.anko.find

/**
 * Created by 1 on 17.07.2017.
 */
class PlayerFragment: Fragment() {
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
        image.setImageBitmap(getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
        RxListener()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_player, container, false)
        image = view.find(R.id.image)
        title = view.find(R.id.title)
        artist = view.find(R.id.artist)
        playPause = view.find(R.id.playPause)
        return view
    }

    fun getRoundedShape(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = 0xff424242.toInt()
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(),
                (bitmap.width / 2).toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
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
            title.text = it.track.title
            artist.text = it.track.artist

            val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, it.track.albumId))
            if(bitmap != null) image.setImageBitmap(getRoundedShape(bitmap)) else {
                image.setImageBitmap(getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
            }

            when(it.action) {
                0 -> {
                    title.text = getString(R.string.no_title)
                    artist.text = getString(R.string.no_artist)
                    image.setImageBitmap(getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
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
        })
    }

    override fun onDestroy() {
        RxServiceActivity.instance()?.getServiceActivity()?.onComplete()
        RxServiceActivity.instance()?.createServiceActivity()
        super.onDestroy()
    }

}