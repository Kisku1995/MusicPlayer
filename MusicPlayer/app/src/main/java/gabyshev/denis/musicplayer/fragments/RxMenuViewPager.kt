package gabyshev.denis.musicplayer.fragments

import io.reactivex.subjects.PublishSubject

/**
 * Created by 1 on 17.07.2017.
 */

class RxMenuViewPager {
    var menuViewPager = PublishSubject.create<Int>()
    var viewPagerMenu = PublishSubject.create<Int>()

    companion object {
        private var instance: RxMenuViewPager? = null

        fun instance(): RxMenuViewPager? {
            if(instance == null) {
                instance = RxMenuViewPager()
            }
            return instance!!
        }
    }

    fun setFromMenuViewPager(position: Int) {
        menuViewPager.onNext(position)
    }

    fun getFromMenuViewPager(): PublishSubject<Int>? = menuViewPager

    fun setFromViewPagerMenu(position: Int) {
        viewPagerMenu.onNext(position)
    }

    fun getFromViewPagerMenu(): PublishSubject<Int>? = viewPagerMenu
}
