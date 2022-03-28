package com.anaykamat.examples.android.tdd.kotlin_data.events

import com.anaykamat.examples.android.tdd.kotlin_data.models.Note

sealed class HomeViewEvents {
    object AddButtonClicked: HomeViewEvents()
    object DeleteButtonCLicked:HomeViewEvents()
    data class NoteSubmitted(val note: Note): HomeViewEvents()
    object DialogDismissed: HomeViewEvents()
    data class NoteDeleted(val note: Note): HomeViewEvents()
    object DeletionDialogDismissed: HomeViewEvents()
}