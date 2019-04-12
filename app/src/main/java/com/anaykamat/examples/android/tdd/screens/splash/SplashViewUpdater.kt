package com.anaykamat.examples.android.tdd.screens.splash

import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import com.anaykamat.examples.android.tdd.state.ViewUpdater

/**
 * Created by anay on 08/08/18.
 */
class SplashViewUpdater: ViewUpdater {

    private val events: PublishSubject<Event> = PublishSubject.create()

    override fun eventsObservable(): Observable<Event> {
        return events.hide().share()
    }

    override fun update(state: State, activity: MainActivity) {
        state.actions.observeOn(AndroidSchedulers.mainThread()).subscribe{ action ->
            when(action){
                is Action.LoadData -> action.dataLoader.subscribe { events.onNext(Event.DataLoaded) }
                Action.ShowCurrentScreen -> {
                    activity.updateView(state.currentScreen.buildView(activity))
                }
                Action.ShowToast -> Toast.makeText(activity,"Done",Toast.LENGTH_SHORT).show()
            }
        }
    }
}