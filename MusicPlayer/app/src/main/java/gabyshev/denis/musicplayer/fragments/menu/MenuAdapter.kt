package gabyshev.denis.musicplayer.fragments.menu

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.fragments.RxMenuViewPager

/**
 * Created by 1 on 17.07.2017.
 */
class MenuAdapter(private val context: Context, private val manager: LinearLayoutManager): RecyclerView.Adapter<MenuHolder>() {
    private val TAG = "MenuAdapter"
    private val menuItems = arrayOf("tracks", "playlists", "albums", "artists", "genres")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MenuHolder {
        RxListener()
        return MenuHolder(LayoutInflater.from(context).inflate(R.layout.activity_main_menu, parent, false))
    }

    override fun onBindViewHolder(holder: MenuHolder?, position: Int) {
        holder?.setHolder(menuItems[position])
    }

    override fun getItemCount(): Int = menuItems.size

    private fun RxListener() {
        RxMenuViewPager.instance()?.getFromViewPagerMenu()?.subscribe({
            Log.d(TAG, "getFromViewPagerMenu ${it}")
            val firstVisible = manager.findFirstVisibleItemPosition()
            val lastVisible = manager.findLastVisibleItemPosition()

            for(i in firstVisible..lastVisible) {
                MenuHolder(manager.findViewByPosition(i)).unselected()
            }

            MenuHolder(manager.findViewByPosition(it)).selected()
        })
    }
}