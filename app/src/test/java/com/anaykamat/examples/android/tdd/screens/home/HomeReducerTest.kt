package com.anaykamat.examples.android.tdd.screens.home

import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.BuildConfig
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.kotlin_data.state.State
import java.time.Instant
import java.util.*

/**
 * Created by anay on 09/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class HomeReducerTest {

    fun reducer(): HomeReducer = HomeReducer()

    @Test
    fun itShouldLoadShowDialogOnClickOfAddButton(){
        val currentState = State()

        val newState = reducer().reduce(currentState, Event.AddButtonClicked)

        val actionList = newState.actions.toList()
        Assert.assertEquals(1, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.ShowDialog) } != null)
    }

    @Test
    fun itShouldCloseTheDialogAndAddNoteOnceNoteIsSubmittedFromDialog(){
        val currentState = State()
        val note = Note("First", Date.from(Instant.now()))

        val newState = reducer().reduce(currentState, Event.NoteSubmitted(note))

        val actionList = newState.actions.toList()
        Assert.assertEquals(2, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.AddNote(note)) } != null)
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.CloseDialog) } != null)
    }

    @Test
    fun itShouldCloseTheDialogOnceDialogIsCancelled(){
        val currentState = State()

        val newState = reducer().reduce(currentState, Event.DialogCancelled)

        val actionList = newState.actions.toList()
        Assert.assertEquals(1, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.CloseDialog) } != null)
    }
}