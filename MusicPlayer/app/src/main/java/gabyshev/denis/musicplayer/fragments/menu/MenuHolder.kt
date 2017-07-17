package gabyshev.denis.musicplayer.fragments.menu

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import gabyshev.denis.musicplayer.R

/**
 * Created by 1 on 17.07.2017.
 */
class MenuHolder(private var context: Context, private var view: View) : RecyclerView.ViewHolder(view) {
    private val text: TextView = view.findViewById(R.id.text)

    fun setHolder(text: String) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.fragment_tracks_text))
        this.text.setTextColor(R.color.colorPrimaryDark)
        this.text.text = text.toUpperCase()
    }

    fun selected() {
        text.setTextColor(ContextCompat.getColor(context, R.color.fragment_tracks_text))
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    fun unselected() {
        text.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.fragment_tracks_text))
    }
}