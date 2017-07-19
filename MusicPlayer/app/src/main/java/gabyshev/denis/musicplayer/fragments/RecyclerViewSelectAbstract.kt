package gabyshev.denis.musicplayer.fragments

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import gabyshev.denis.musicplayer.events.EnumSelectStatus
import gabyshev.denis.musicplayer.fragments.select.SelectListener
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.TracksHelper
import io.reactivex.disposables.CompositeDisposable
import java.util.ArrayList

/**
 * Created by 1 on 19.07.2017.
 */
abstract class RecyclerViewSelectAbstract<T, K : RecyclerView.ViewHolder?>(
                               private val context: Context,
                               private val arrayObject: ArrayList<T>,
                               private val rxBus: RxBus,
                               private val subscriptions: CompositeDisposable) : RecyclerView.Adapter<K>() {

    var selectedObject = ArrayList<T>()
    var selectListener: SelectListener = context as SelectListener

    init {
        RxSelectListener()
    }

    fun RxSelectListener() {
        subscriptions.addAll(
                rxBus.toObservable()
                        .subscribe({
                            if(it is EnumSelectStatus && it == EnumSelectStatus.CANCEL) {
                                cancelSelecting()
                            }
                        }),
                rxBus.toObservable()
                        .subscribe({
                            if(it is EnumSelectStatus && it == EnumSelectStatus.ADD) {
                                addSelecting()
                            }
                        })
        )
    }

    fun checkHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isContainsTrack(position)) {
            TracksHelper.instance().setBackground(context, holder.itemView, position)
        } else {
            TracksHelper.instance().setSelectedBackground(context, holder.itemView, position)
        }
        if(selectedObject.size >= 1) selectListener.countSelect((selectedObject.size).toString())
    }

    fun cancelSelecting() {
        selectedObject.clear()
        selectListener.stopSelect()
        notifyDataSetChanged()
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): K

    abstract override fun onBindViewHolder(holder: K?, position: Int)

    override fun getItemCount(): Int = arrayObject.size

    abstract fun isContainsTrack(position: Int): Boolean

    abstract fun addSelecting()


}