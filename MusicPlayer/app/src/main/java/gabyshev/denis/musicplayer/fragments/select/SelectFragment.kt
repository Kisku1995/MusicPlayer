package gabyshev.denis.musicplayer.fragments.select

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumSelectStatus
import gabyshev.denis.musicplayer.utils.RxBus
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by Borya on 18.07.2017.
 */
class SelectFragment: Fragment() {
    private lateinit var selectCount: TextView
    private lateinit var cancel: ImageView
    private lateinit var add: ImageView

    @Inject lateinit var rxBus: RxBus

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectCount.text = "1 ${getString(R.string.items_selected)}"

        (context.applicationContext as App).component.inject(this)

        cancel.setOnClickListener {
            rxBus.send(EnumSelectStatus.CANCEL)
        }

        add.setOnClickListener {
            rxBus.send(EnumSelectStatus.ADD)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_select, container, false)

        selectCount = view.find(R.id.selectCount)
        cancel = view.find(R.id.cancel)
        add = view.find(R.id.add)

        return view
    }

    fun selectCount(text: String) {
        selectCount.text = "$text ${getString(R.string.items_selected)}"
    }


}