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
class HomeReducer : Reducer {
    override fun reduce(currentState: State, event: Event): State {
        return when (event) {
            is Event.AddButtonClicked -> currentState.copy(actions = Observable.fromIterable(listOf(
                Action.ShowDialog)))
            is Event.NoteSubmitted -> {
                val (actions, notes) = if (currentState.notes.contains(event.note)) {
                    Pair(Observable.just(Action.ShowToast), emptyList<Note>())
                } else {
                    Pair(Observable.fromIterable(listOf(Action.CloseDialog,
                        Action.AddNote(event.note))), listOf(event.note))
                }
                currentState.copy(actions = actions as Observable<Action>,
                    notes = (currentState.notes + notes))
            }
            is Event.DialogCancelled -> currentState.copy(actions = Observable.fromIterable(listOf(
                Action.CloseDialog)))
            is Event.DeleteButtonClicked -> {
                val actions = if (currentState.notes.isNullOrEmpty()) {
                    Observable.fromIterable(
                        listOf(Action.ShowEmptyListToast))
                } else {
                    Observable.fromIterable(
                        listOf(Action.ShowDeletionDialog(currentState.notes.first())))
                }

                currentState.copy(actions = actions as Observable<Action>)
            }
            is Event.NoteDeleted -> {
                val (actions, notes) = if (currentState.notes.contains(event.note)) {
                    Pair(Observable.fromIterable(listOf(Action.CloseDeletionDialog,
                        Action.DeleteNote(event.note))), listOf(event.note))
                } else {
                    Pair(Observable.just(Action.ShowDeletionErrorToast), emptyList<Note>())
                }
                currentState.copy(actions = actions as Observable<Action>,
                    notes = (currentState.notes - notes))
            }
            is Event.DeletionDialogCancelled -> currentState.copy(actions = Observable.fromIterable(
                listOf(
                    Action.CloseDeletionDialog)))
            else -> currentState
        }
    }
}