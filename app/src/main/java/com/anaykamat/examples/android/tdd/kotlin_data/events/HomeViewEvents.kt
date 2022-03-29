package com.anaykamat.examples.android.tdd.kotlin_data.events

import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import java.text.FieldPosition

sealed class HomeViewEvents {
    object AddButtonClicked: HomeViewEvents()
    data class NoteSubmitted(val note: Note): HomeViewEvents()
    data class RemoveNote(val position: Int): HomeViewEvents()
    object DialogDismissed: HomeViewEvents()
}