package com.anaykamat.examples.android.tdd.screens.home

import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import com.anaykamat.examples.android.tdd.state.ViewUpdater
import com.anaykamat.examples.android.tdd.views.HomeView

/**
 * Created by anay on 09/08/18.
 */
class HomeViewUpdater : ViewUpdater {
    private val events: PublishSubject<Event> = PublishSubject.create()

    override fun eventsObservable(): Observable<Event> = events.hide().share()

    override fun update(state: State, activity: MainActivity) {
        state.actions.observeOn(AndroidSchedulers.mainThread()).subscribe { action ->
            when (action) {
                is Action.ShowDialog -> (activity.currentView() as HomeView).showDialogForNewTodo(
                    activity)
                is Action.CloseDialog -> (activity.currentView() as HomeView).dismissDialog()
                is Action.AddNote -> (activity.currentView() as HomeView).add(action.note)
                Action.ShowCurrentScreen -> {
                    activity.updateView(state.currentScreen.buildView(activity))
                }
                Action.ShowToast -> Toast.makeText(activity,
                    "Note already exists!",
                    Toast.LENGTH_SHORT).show()
                Action.CloseDeletionDialog -> (activity.currentView() as HomeView).dismissDeletionDialog()
                is Action.DeleteNote -> (activity.currentView() as HomeView).deleteNote(action.note)
                is Action.ShowDeletionDialog -> (activity.currentView() as HomeView).showDialogForNoteDeletion(
                    activity,
                    action.note)
                Action.ShowDeletionErrorToast -> Toast.makeText(activity,
                    "Note does not exist!",
                    Toast.LENGTH_SHORT).show()
                Action.ShowEmptyListToast -> Toast.makeText(activity,
                    "Notes empty!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}