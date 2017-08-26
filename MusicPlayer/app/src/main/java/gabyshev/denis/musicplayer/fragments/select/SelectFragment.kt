package gabyshev.denis.musicplayer.fragments.select

import android.app.Activity
import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_select.*
import org.jetbrains.anko.find
import javax.inject.Inject

/**
 * Created by Borya on 18.07.2017.
 */
class SelectFragment: Fragment() {
    @Inject lateinit var rxBus: RxBus


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_select, container, false)
    }

    fun selectCount(text: String) {
        selectCount.text = "$text ${getString(R.string.items_selected)}"
    }


}