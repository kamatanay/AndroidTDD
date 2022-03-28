package com.anaykamat.examples.android.tdd.kotlin_data.events

import com.anaykamat.examples.android.tdd.kotlin_data.models.Note

/**
 * Created by anay on 08/08/18.
 */
sealed class Event {
    object LoadData : Event()
    object DataLoaded : Event()
    object AddButtonClicked : Event()
    object DeleteButtonClicked : Event()
    data class NoteSubmitted(val note: Note) : Event()
    object DialogCancelled : Event()
    data class NoteDeleted(val note: Note):Event()
    object DeletionDialogCancelled : Event()

}