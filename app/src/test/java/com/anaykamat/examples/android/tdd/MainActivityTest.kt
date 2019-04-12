package com.anaykamat.examples.android.tdd

import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.screens.Screen
import com.anaykamat.examples.android.tdd.state.Reducer
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import com.anaykamat.examples.android.tdd.state.ViewUpdater
import com.anaykamat.examples.android.tdd.BuildConfig
import com.anaykamat.examples.android.tdd.R

/**
 * Created by anay on 10/08/18.
 */

class MockMainActivity: MainActivity(){
    val currentScreen = Mockito.mock(Screen::class.java)
    val nextScreen = Mockito.mock(Screen::class.java)
    val state = State(currentScreen = currentScreen)
    val nextState = State(currentScreen = nextScreen)
    val reducer = Mockito.mock(Reducer::class.java)
    val viewUpdater = Mockito.mock(ViewUpdater::class.java)
    val nextScreenViewUpdater = Mockito.mock(ViewUpdater::class.java)

    val events:PublishSubject<Event> = PublishSubject.create()

    init {
        Mockito.`when`(currentScreen.reducer()).thenReturn(reducer)
        Mockito.`when`(currentScreen.updater()).thenReturn(viewUpdater)
        Mockito.`when`(nextScreen.updater()).thenReturn(nextScreenViewUpdater)

        Mockito.`when`(reducer.reduce(state, Event.DataLoaded)).thenReturn(nextState)

    }

    override fun buildInitialState(): Pair<State, List<Observable<Event>>> {
        return Pair(state, listOf(events.hide().share()))
    }
}

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private fun activity(): MainActivity = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .visible().get()

    @Test
    fun itShouldAllowToAddAGivenViewToContainer(){
        val activity = activity()
        val view = TextView(activity).also {
            it.text = "First"
        }

        activity.updateView(view)

        Assert.assertEquals(view, activity.findViewById<LinearLayout>(R.id.rootLayout).getChildAt(0))
    }

    @Test
    fun itShouldReturnTheViewInTheContainer(){
        val activity = activity()
        val view = TextView(activity).also {
            it.text = "First"
        }

        activity.findViewById<LinearLayout>(R.id.rootLayout).also { it.removeAllViews() }.addView(view)

        Assert.assertEquals(view, activity.currentView())
    }

    @Test
    fun itShouldImplementTheFlowOfEventStateProcessing(){

        val mockMainActivity = Robolectric.buildActivity(MockMainActivity::class.java)
                .create()
                .start()
                .resume()
                .visible().get()

        mockMainActivity.events.onNext(Event.DataLoaded)

        Mockito.verify(mockMainActivity.nextScreenViewUpdater, times(1)).update(mockMainActivity.nextState, mockMainActivity)
    }

}