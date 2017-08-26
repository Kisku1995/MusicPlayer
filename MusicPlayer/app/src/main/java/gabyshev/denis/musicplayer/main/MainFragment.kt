package gabyshev.denis.musicplayer.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gabyshev.denis.musicplayer.App
import gabyshev.denis.musicplayer.R
import gabyshev.denis.musicplayer.events.EnumSelectStatus
import gabyshev.denis.musicplayer.fragments.PlayerViewPagerAdapter
import gabyshev.denis.musicplayer.fragments.ZoomOutPageTransformer
import gabyshev.denis.musicplayer.fragments.player.MainPlayerFragment
import gabyshev.denis.musicplayer.fragments.select.SelectFragment
import gabyshev.denis.musicplayer.fragments.select.SelectListener
import gabyshev.denis.musicplayer.utils.RxBus
import gabyshev.denis.musicplayer.utils.app
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.support.v4.act
import javax.inject.Inject

/**
 * Created by Borya on 26.08.2017.
 */
class MainFragment : Fragment(), SelectListener {
    @Inject lateinit var rxBus: RxBus
    @Inject lateinit var player: MainPlayerFragment

    private var selectFragment = SelectFragment()

    private var isSelect: Boolean = false

    private lateinit var supportFragmentManager : FragmentManager

    private lateinit var callback: SelectListener

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context.applicationContext as App).component.inject(this)

        supportFragmentManager = activity.supportFragmentManager

        setupActivity()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        callback = activity as SelectListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun startSelect() {
        supportFragmentManager.beginTransaction().replace(R.id.player, selectFragment).commit()
        isSelect = true
    }

    override fun stopSelect() {
        supportFragmentManager.beginTransaction().replace(R.id.player, player).commit()
        isSelect = false
    }

    override fun countSelect(count: String) {
        try {
            selectFragment.selectCount(count)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupActivity() {
        viewPager.adapter = PlayerViewPagerAdapter(childFragmentManager)
        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
        menu.setupWithViewPager(viewPager)

        childFragmentManager.beginTransaction().replace(R.id.player, player).commit()

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if(isSelect) rxBus.send(EnumSelectStatus.CANCEL)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

}