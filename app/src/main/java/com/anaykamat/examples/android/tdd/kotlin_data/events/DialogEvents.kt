package com.anaykamat.examples.android.tdd.kotlin_data.events

sealed class DialogEvents {
    data class OkClicked(val note:String): DialogEvents()
    object CancelClicked: DialogEvents()
}