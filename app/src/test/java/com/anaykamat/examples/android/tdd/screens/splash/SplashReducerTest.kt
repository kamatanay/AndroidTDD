package com.anaykamat.examples.android.tdd.screens.splash

import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.BuildConfig
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.screens.HomeScreen
import com.anaykamat.examples.android.tdd.screens.Screen
import com.anaykamat.examples.android.tdd.screens.SplashScreen
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import java.util.concurrent.TimeUnit

/**
 * Created by anay on 08/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class SplashReducerTest {

    private fun reducer(): SplashReducer = SplashReducer()

    private fun initialState(): State {
        val screens = listOf<Screen>(SplashScreen(), HomeScreen())
        return State(currentScreen = screens.first(), splashScreen = screens.first() as SplashScreen, homeScreen = screens.last() as HomeScreen)
    }

    @Test
    fun itShouldShowSplashScreenAndLoadData(){
        val reducer = reducer()
        val nextState = reducer.reduce(initialState(), Event.LoadData)

        Assert.assertEquals(nextState.splashScreen, nextState.currentScreen)
        val actionList = nextState.actions.toList()
        Assert.assertEquals(2, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.ShowCurrentScreen) } != null)
        val loadDataAction = actionList.blockingGet().find { action -> action is Action.LoadData }
        Assert.assertTrue(loadDataAction != null)
        (loadDataAction as Action.LoadData).dataLoader.timeout(6, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun itShouldSwitchToHomeScreenOnDataLoad(){
        val reducer = reducer()
        val nextState = reducer.reduce(initialState(), Event.DataLoaded)

        Assert.assertEquals(nextState.homeScreen, nextState.currentScreen)
        val actionList = nextState.actions.toList()
        Assert.assertEquals(1, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.ShowCurrentScreen) } != null)
    }

}