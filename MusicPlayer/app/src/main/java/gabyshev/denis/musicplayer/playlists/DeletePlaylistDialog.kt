package gabyshev.denis.musicplayer.playlists

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumRVAdapterStatus
import gabyshev.denis.musicplayer.events.RVAdapterStatus
import gabyshev.denis.musicplayer.utils.RxBus
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by 1 on 19.07.2017.
 */
class DeletePlaylistDialog : DialogFragment() {
    private val TAG = "DeletePlaylistDialog"

    private lateinit var yes: Button
    private lateinit var no: Button
    private lateinit var playlistName: TextView

    @Inject lateinit var rxBus: RxBus

    private var id: Long = -1L
    private var bundleTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (context.applicationContext as App).component.inject(this)

        getBundle()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistName.text = "${context.getString(R.string.delete_playlist)} $bundleTitle?"

        no.setOnClickListener { dismiss() }

        yes.setOnClickListener {
            PlaylistHelper.instance().deletePlaylist(context, id)
            dismiss()
            activity.finish()
            rxBus.send(RVAdapterStatus(EnumRVAdapterStatus.UPDATE))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_delete_playlist, container, false)

        yes = view.find(R.id.yes)
        no = view.find(R.id.no)
        playlistName = view.find(R.id.playlistName)

        return view
    }

    fun getBundle() {
        val bundle: Bundle? = arguments

        if(bundle != null) {
            id = bundle.getLong("id")
            bundleTitle = bundle.getString("title")

            Log.d(TAG, "$id $bundleTitle")
        } else {
            dismiss()
        }
    }


}