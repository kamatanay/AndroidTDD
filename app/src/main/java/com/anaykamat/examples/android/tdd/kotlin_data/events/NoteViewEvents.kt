package com.anaykamat.examples.android.tdd.kotlin_data.events


sealed class NoteViewEvents {
    object RemoveClicked:NoteViewEvents()
}