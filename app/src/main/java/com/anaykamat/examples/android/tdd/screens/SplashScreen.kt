package com.anaykamat.examples.android.tdd.screens

import android.content.Context
import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.screens.splash.SplashReducer
import com.anaykamat.examples.android.tdd.screens.splash.SplashViewUpdater
import com.anaykamat.examples.android.tdd.state.Reducer
import com.anaykamat.examples.android.tdd.state.ViewUpdater
import com.anaykamat.examples.android.tdd.views.SplashView

/**
 * Created by anay on 08/08/18.
 */
class SplashScreen: Screen {

    private val events: PublishSubject<Event> = PublishSubject.create()

    override fun buildView(context:Context): View {
        return SplashView(context)
    }

    override fun eventsObservable(): Observable<Event> {
        return events.hide().share()
    }

    override fun reducer(): Reducer {
        return SplashReducer()
    }

    override fun updater(): ViewUpdater {
        val splashViewUpdater = SplashViewUpdater()
        splashViewUpdater.eventsObservable().subscribe { event ->
            events.onNext(event)
        }
        return splashViewUpdater
    }

}