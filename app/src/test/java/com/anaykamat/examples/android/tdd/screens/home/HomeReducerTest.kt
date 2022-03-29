package com.anaykamat.examples.android.tdd.screens.home

import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.anaykamat.examples.android.tdd.kotlin_data.events.Event
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.anaykamat.examples.android.tdd.kotlin_data.state.Action
import com.anaykamat.examples.android.tdd.kotlin_data.state.State

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
        val note = Note("First")

        val newState = reducer().reduce(currentState, Event.NoteSubmitted(note))

        val actionList = newState.actions.toList()
        Assert.assertEquals(2, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.AddNote(note)) } != null)
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.CloseDialog) } != null)
    }

    @Test
    fun itShouldRecordTheNoteAddedInTheState(){
        val currentState = State()
        val note = Note("First")

        val newState = reducer().reduce(currentState, Event.NoteSubmitted(note))

        Assert.assertEquals(listOf<Note>(Note("First")), newState.notes)
    }

    @Test
    fun itRemoveTheNoteFromStateAndUIIfRemoveNoteIsRequessted(){
        val note = Note("First")

        val currentState = State(notes = listOf(note))

        val newState = reducer().reduce(currentState, Event.NoteRemoveRequested(0))

        Assert.assertEquals(listOf<Note>(), newState.notes)
        val actionList = newState.actions.toList()
        Assert.assertEquals(1, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.RemoveNoteAt(0)) } != null)
    }

    @Test
    fun itShouldNotAddDuplicateNote(){
        val currentState = State(notes = listOf(Note("First")))
        val note = Note("First")

        val newState = reducer().reduce(currentState, Event.NoteSubmitted(note))

        Assert.assertEquals(listOf<Note>(Note("First")), newState.notes)
        val actionList = newState.actions.toList()
        Assert.assertEquals(1, actionList.blockingGet().count())
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.AddNote(note)) } == null)
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.CloseDialog) } == null)
        Assert.assertTrue(actionList.blockingGet().find { action -> action.equals(Action.ShowToast) } != null)
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