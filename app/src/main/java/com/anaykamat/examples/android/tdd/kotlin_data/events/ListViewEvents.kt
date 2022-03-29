package com.anaykamat.examples.android.tdd.kotlin_data.events


sealed class ListViewEvents {
    data class RemoveNote(val position:Int, val note:String):ListViewEvents()
}