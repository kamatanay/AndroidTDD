package com.anaykamat.examples.android.tdd.state

import io.reactivex.Observable
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.state.State

/**
 * Created by anay on 08/08/18.
 */
interface ViewUpdater {
    fun eventsObservable(): Observable<Event>
    fun update(state: State, activity: MainActivity)
}