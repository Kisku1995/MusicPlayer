package gabyshev.denis.musicplayer.playlists

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumRVAdapterStatus
import gabyshev.denis.musicplayer.events.RVAdapterStatus
import gabyshev.denis.musicplayer.utils.RxBus
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by 1 on 19.07.2017.
 */
class AddPlaylistDialog : DialogFragment() {
    private lateinit var positive: Button
    private lateinit var negative: Button
    private lateinit var playlistName : EditText

    @Inject lateinit var rxBus: RxBus

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (context.applicationContext as App).component.inject(this)

        positive.setOnClickListener {
            if(playlistName.text.isNotEmpty()) {
                PlaylistHelper.instance().createPlaylist(context, playlistName.text.toString())
                rxBus.send(RVAdapterStatus(EnumRVAdapterStatus.UPDATE))
                dismiss()
            }
        }

        negative.setOnClickListener { dismiss() }


    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_add_playlist, container, false)

        positive = view.find(R.id.positive)
        negative = view.find(R.id.negative)
        playlistName = view.find(R.id.playlistName)

        return view
    }
}