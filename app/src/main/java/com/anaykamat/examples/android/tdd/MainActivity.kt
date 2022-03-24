package com.anaykamat.examples.android.tdd

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import com.anaykamat.examples.android.tdd.R


open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val activity = this

        val (initialState, listOfEventObservables) = buildInitialState()

        Observable.merge(listOfEventObservables)
                .scan(initialState, { state, event ->
                     state.currentScreen.reducer().reduce(state, event)
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state ->
                    state.currentScreen.updater().update(state, activity)
                }
    }

    open fun currentView(): View {
        return findViewById<LinearLayout>(R.id.rootLayout).getChildAt(0)
    }

    fun updateView(view:View){
        findViewById<LinearLayout>(R.id.rootLayout).removeAllViews()
        findViewById<LinearLayout>(R.id.rootLayout).addView(view)
    }

    open protected fun buildInitialState():Pair<State, List<Observable<out Event>>>{
        return State().let { it.copy(currentScreen = it.splashScreen) }.let {
            Pair(it, listOf(it.splashScreen.eventsObservable(), it.homeScreen.eventsObservable(), Observable.just(Event.LoadData)))
        }
    }
}
