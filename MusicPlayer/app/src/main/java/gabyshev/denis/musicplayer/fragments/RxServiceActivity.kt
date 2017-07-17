package gabyshev.denis.musicplayer.fragments

import io.reactivex.subjects.PublishSubject

/**
 * Created by 1 on 17.07.2017.
 */

class RxServiceActivity {
    var serviceActivity1 = PublishSubject.create<ServiceActivity>()

    companion object {
        private var instance: RxServiceActivity? = null

        fun instance(): RxServiceActivity? {
            if(instance == null) {
                instance = RxServiceActivity()
            }
            return instance!!
        }
    }

    fun setServiceActivity(serviceActivity: ServiceActivity) {
        serviceActivity1.onNext(serviceActivity)
    }

    fun getServiceActivity(): PublishSubject<ServiceActivity> = serviceActivity1
}
