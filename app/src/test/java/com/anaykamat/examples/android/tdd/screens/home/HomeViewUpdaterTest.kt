package com.anaykamat.examples.android.tdd.screens.home

import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import com.anaykamat.examples.android.tdd.views.HomeView
import junit.framework.Assert
import org.robolectric.Robolectric
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.ShadowToast

/**
 * Created by anay on 09/08/18.
 */

@RunWith(RobolectricTestRunner::class)
class HomeViewUpdaterTest {


    private fun viewUpdater(): HomeViewUpdater = HomeViewUpdater()

    @Test
    fun showDialogShouldShowTheDialogToAddTodo() {
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.ShowDialog))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Mockito.verify(mockHomeView, Mockito.times(1)).showDialogForNewTodo(mainActivity)

    }

    @Test
    fun deleteDialogShouldShowTheDialogToDeleteNote() {
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.ShowDeletionDialog(Note("Hi"))))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Mockito.verify(mockHomeView, Mockito.times(1))
            .showDialogForNoteDeletion(mainActivity, Note("Hi"))

    }

    @Test
    fun closeDialogShouldDismissTheDialogToAddTodo() {
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.CloseDialog))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Mockito.verify(mockHomeView, Mockito.times(1)).dismissDialog()

    }

    @Test
    fun closeDeletionDialogShouldDismissTheDeletionDialogToDeleteNote() {
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.CloseDeletionDialog))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Mockito.verify(mockHomeView, Mockito.times(1)).dismissDeletionDialog()

    }

    @Test
    fun addNoteShouldAddTheNoteInTheView() {

        val note = Note("Hello")
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.AddNote(note)))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Mockito.verify(mockHomeView, Mockito.times(1)).add(note)
    }

    @Test
    fun deleteNoteShouldAddTheNoteFromTheView() {

        val note = Note("Hi")
        val mockHomeView = Mockito.mock(HomeView::class.java)

        val mainActivity = Mockito.mock(MainActivity::class.java)

        Mockito.`when`(mainActivity.currentView()).thenReturn(mockHomeView)

        val state = State(actions = Observable.just(Action.DeleteNote(note)))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Mockito.verify(mockHomeView, Mockito.times(1)).deleteNote(note)
    }


    @Test
    fun showToastShowTellTheUserThatNoteAlreadyExists() {

        val mainActivity = Robolectric.setupActivity(MainActivity::class.java)

        val state = State(actions = Observable.just(Action.ShowToast))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Assert.assertEquals("Note already exists!", ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun showToastShowTellTheUserThatNoteDoesNotExists() {

        val mainActivity = Robolectric.setupActivity(MainActivity::class.java)

        val state = State(actions = Observable.just(Action.ShowDeletionErrorToast))

        viewUpdater().update(state, mainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Assert.assertEquals("Note does not exist!", ShadowToast.getTextOfLatestToast())
    }


}