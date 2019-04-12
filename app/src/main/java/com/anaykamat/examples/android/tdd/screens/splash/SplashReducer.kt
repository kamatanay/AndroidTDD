package com.anaykamat.examples.android.tdd.screens.splash

import io.reactivex.Observable
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.state.Reducer
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import java.util.concurrent.TimeUnit

/**
 * Created by anay on 08/08/18.
 */
class SplashReducer: Reducer {
    override fun reduce(currentState: State, event: Event): State {
        return when(event){
            is Event.LoadData -> currentState.copy(actions = Observable.fromIterable(listOf(Action.ShowCurrentScreen, Action.LoadData(
                    Observable.timer(5, TimeUnit.SECONDS).map { Unit }
            ))))
            Event.DataLoaded -> currentState.copy(currentScreen = currentState.homeScreen, actions = Observable.fromIterable(listOf(Action.ShowCurrentScreen)))
            else -> currentState
        }
    }
}