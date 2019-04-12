package com.anaykamat.examples.android.tdd.screens.home

import io.reactivex.Observable
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.state.Reducer
import com.anaykamat.examples.android.tdd.kotlin_data.state.State

/**
 * Created by anay on 09/08/18.
 */
class HomeReducer: Reducer {
    override fun reduce(currentState: State, event: Event): State {
        return when(event){
            is Event.AddButtonClicked -> currentState.copy(actions = Observable.fromIterable(listOf(Action.ShowDialog)))
            is Event.NoteSubmitted -> currentState.copy(actions = Observable.fromIterable(listOf(Action.CloseDialog, Action.AddNote(event.note))))
            is Event.DialogCancelled -> currentState.copy(actions = Observable.fromIterable(listOf(Action.CloseDialog)))
            else -> currentState
        }
    }
}