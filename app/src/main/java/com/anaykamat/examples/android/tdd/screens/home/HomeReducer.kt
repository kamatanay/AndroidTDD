package com.anaykamat.examples.android.tdd.screens.home

import io.reactivex.Observable
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
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
            is Event.NoteSubmitted -> {
                val (actions, notes) = if (currentState.notes.contains(event.note)){
                    Pair(Observable.just(Action.ShowToast), emptyList<Note>())
                }else{
                    Pair(Observable.fromIterable(listOf(Action.CloseDialog, Action.AddNote(event.note))), listOf(event.note))
                }
                currentState.copy(actions = actions as Observable<Action>, notes = (currentState.notes + notes))
            }
            is Event.DialogCancelled -> currentState.copy(actions = Observable.fromIterable(listOf(Action.CloseDialog)))
            is Event.NoteRemoveRequested -> currentState.copy(
                    notes = currentState.notes.filterIndexed {index,_ -> index != event.position},
                    actions = Observable.fromIterable(listOf(Action.RemoveNoteAt(event.position)))
            )
            else -> currentState
        }
    }
}