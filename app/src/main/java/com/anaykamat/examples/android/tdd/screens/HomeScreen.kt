package com.anaykamat.examples.android.tdd.screens

import android.content.Context
import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.events.HomeViewEvents
import com.anaykamat.examples.android.tdd.screens.home.HomeReducer
import com.anaykamat.examples.android.tdd.screens.home.HomeViewUpdater
import com.anaykamat.examples.android.tdd.state.Reducer
import com.anaykamat.examples.android.tdd.state.ViewUpdater
import com.anaykamat.examples.android.tdd.views.HomeView

/**
 * Created by anay on 09/08/18.
 */
class HomeScreen : Screen {

    private val events: PublishSubject<Event> = PublishSubject.create()

    override fun buildView(context: Context): View {
        return HomeView(context).also {
            it.eventsObservable().subscribe { event ->
                when (event) {
                    is HomeViewEvents.DialogDismissed -> events.onNext(Event.DialogCancelled)
                    is HomeViewEvents.NoteSubmitted -> events.onNext(Event.NoteSubmitted(event.note))
                    is HomeViewEvents.AddButtonClicked -> events.onNext(Event.AddButtonClicked)
                    is HomeViewEvents.DeleteButtonCLicked -> {
                        events.onNext(Event.DeleteButtonClicked)}
                    HomeViewEvents.DeletionDialogDismissed -> events.onNext(Event.DeletionDialogCancelled)
                    is HomeViewEvents.NoteDeleted -> events.onNext(Event.NoteDeleted(event.note))
                }
            }
        }
    }

    override fun eventsObservable(): Observable<Event> = events.hide().share()

    override fun reducer(): Reducer = HomeReducer()

    override fun updater(): ViewUpdater {
        return HomeViewUpdater()
    }
}