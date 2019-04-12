package com.anaykamat.examples.android.tdd.screens.home

import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.BuildConfig
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import com.anaykamat.examples.android.tdd.views.HomeView
import java.time.Instant
import java.util.*

/**
 * Created by anay on 09/08/18.
 */

@RunWith(RobolectricTestRunner::class)
class HomeViewUpdaterTest {


    private fun viewUpdater(): HomeViewUpdater = HomeViewUpdater()

    @Test
    fun showDialogShouldShowTheDialogToAddTodo(){
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.ShowDialog))

        viewUpdater().update(state, mainActivity)

        Mockito.verify(mockHomeView, Mockito.times(1)).showDialogForNewTodo(mainActivity)

    }

    @Test
    fun closeDialogShouldDismissTheDialogToAddTodo(){
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.CloseDialog))

        viewUpdater().update(state, mainActivity)

        Mockito.verify(mockHomeView, Mockito.times(1)).dismissDialog()

    }

    @Test
    fun addNoteShouldAddTheNoteInTheView(){

        val note = Note("Hello", Date.from(Instant.now()))
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.AddNote(note)))

        viewUpdater().update(state, mainActivity)

        Mockito.verify(mockHomeView, Mockito.times(1)).add(note)
    }

}