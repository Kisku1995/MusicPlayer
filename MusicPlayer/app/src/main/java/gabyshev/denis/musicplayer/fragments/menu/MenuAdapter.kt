package gabyshev.denis.musicplayer.fragments.menu

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R

/**
 * Created by 1 on 17.07.2017.
 */
class MenuAdapter(private val context: Context, private val manager: LinearLayoutManager): RecyclerView.Adapter<MenuHolder>() {
    private val TAG = "MenuAdapter"
    private val menuItems = arrayOf("tracks", "playlists", "albums", "artists", "genres")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MenuHolder {
        return MenuHolder(context, LayoutInflater.from(context).inflate(R.layout.activity_main_menu, parent, false))
    }

    override fun onBindViewHolder(holder: MenuHolder?, position: Int) {
        holder?.setHolder(menuItems[position])
    }

    override fun getItemCount(): Int = menuItems.size

    fun setSelectedHolder(position: Int) {
        Log.d(TAG, "getFromViewPagerMenu ${position}")
        val firstVisible = manager.findFirstVisibleItemPosition()
        val lastVisible = manager.findLastVisibleItemPosition()

        for(i in firstVisible..lastVisible) {
            if(position != i) MenuHolder(context, manager.findViewByPosition(i)).unselected()
            else MenuHolder(context, manager.findViewByPosition(i)).selected()
        }

        notifyDataSetChanged()

    }
}