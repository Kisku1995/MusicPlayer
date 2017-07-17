package gabyshev.denis.musicplayer.fragments.menu

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R
import org.jetbrains.anko.find

/**
 * Created by 1 on 17.07.2017.
 */
class MenuHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val view: View = itemView
    private val text: TextView = itemView.find(R.id.text)

    fun setHolder(text: String) {
        this.text.setTextColor(R.color.colorPrimaryDark)
        this.text.text = text.toUpperCase()
    }

    fun selected() {
        text.setTextColor(R.color.fragment_tracks_text)
        view.setBackgroundColor(R.color.colorPrimary)
    }

    fun unselected() {
        text.setTextColor(R.color.colorPrimaryDark)
        view.setBackgroundColor(R.color.fragment_tracks_text)
    }
}