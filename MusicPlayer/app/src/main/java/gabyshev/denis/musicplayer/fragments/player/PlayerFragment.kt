package gabyshev.denis.musicplayer.fragments.player

import android.app.Fragment
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.fragments.RxServiceActivity
import gabyshev.denis.musicplayer.fragments.tracks.TracksHelper
import gabyshev.denis.musicplayer.service.TrackData
import org.jetbrains.anko.find

/**
 * Created by 1 on 17.07.2017.
 */
class PlayerFragment: Fragment() {
    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var artist: TextView

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
        RxServiceActivity.instance()?.getServiceActivity()?.subscribe({
            title.text = it.track.title
            artist.text = it.track.artist

            val bitmap: Bitmap? = BitmapFactory.decodeFile(TracksHelper.instance().getAlbumImagePath(context, it.track.albumId))
            if(bitmap != null) image.setImageBitmap(getRoundedShape(bitmap)) else {
                image.setImageBitmap(getRoundedShape(TracksHelper.instance().getNoAlbumBitmap()))
            }
        })
    }
}