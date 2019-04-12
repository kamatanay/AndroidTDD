package com.anaykamat.examples.android.tdd.state

import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.state.State

/**
 * Created by anay on 08/08/18.
 */
interface Reducer {
    fun reduce(currentState: State, event: Event): State
}