package gabyshev.denis.musicplayer.service.activityplayer

import gabyshev.denis.musicplayer.service.TrackData
import io.reactivex.subjects.PublishSubject

/**
 * Created by 1 on 17.07.2017.
 */

class RxServiceActivity {
    private var serviceActivity1 = PublishSubject.create<ServiceActivity>()
    private var activityService1 = PublishSubject.create<Int>()
    var track: ServiceActivity? = null

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

    fun setActivityService(action: Int) {
        activityService1.onNext(action)
    }

    fun getActivityService(): PublishSubject<Int> = activityService1

    fun createActivityService() {
        activityService1 = PublishSubject.create<Int>()
    }

    fun createServiceActivity() {
        serviceActivity1 = PublishSubject.create<ServiceActivity>()
    }


}