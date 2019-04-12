package com.anaykamat.examples.android.tdd.screens.splash

import android.widget.TextView
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.BuildConfig
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import org.robolectric.Robolectric
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.ShadowToast
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.screens.Screen
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import java.util.concurrent.TimeUnit


/**
 * Created by anay on 08/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class SplashViewUpdaterTest {

    private fun viewUpdater(): SplashViewUpdater = SplashViewUpdater()

    private fun mainActivity(): MainActivity {
        return Robolectric.setupActivity(MainActivity::class.java);
    }

    @Test
    fun loadDataShouldRaiseTheEventOnceDataHasBeenLoaded(){
        val state = State(actions = Observable.just(Action.LoadData(Observable.just(Unit))))
        val viewUpdater = viewUpdater()
        Observable.create<Unit> { emitter ->
            viewUpdater.eventsObservable().subscribe { event ->
                if (!(event is Event.DataLoaded)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            viewUpdater.update(state, mainActivity())
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun showCurrentScreenShouldBuildTheViewAndAddItToActivity(){
        val mockScreen = Mockito.mock(Screen::class.java)
        val textView: TextView = TextView(RuntimeEnvironment.application.applicationContext)
        val activity = mainActivity()
        val viewUpdater = viewUpdater()

        Mockito.`when`(mockScreen.buildView(activity)).thenReturn(textView)
        val state = State(currentScreen = mockScreen, actions = Observable.just(Action.ShowCurrentScreen))

        viewUpdater.update(state, activity)

        Assert.assertEquals(textView, activity.currentView())
    }

    @Test
    fun showToastShouldShowTheToast(){
        val activity = mainActivity()
        val viewUpdater = viewUpdater()
        val state = State(actions = Observable.just(Action.ShowToast))

        viewUpdater.update(state, activity)

        Assert.assertEquals("Done",ShadowToast.getTextOfLatestToast())

    }

}