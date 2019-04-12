package com.anaykamat.examples.android.tdd.screens

import android.content.Context
import android.view.View
import io.reactivex.Observable
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.state.Reducer
import com.anaykamat.examples.android.tdd.state.ViewUpdater

/**
 * Created by anay on 08/08/18.
 */
interface Screen {
    fun buildView(context: Context): View
    fun eventsObservable(): Observable<Event>
    fun reducer(): Reducer
    fun updater(): ViewUpdater
}