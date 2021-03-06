package com.anaykamat.examples.android.tdd.kotlin_data.state

import io.reactivex.Observable
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note

/**
 * Created by anay on 08/08/18.
 */
sealed class Action {
    object ShowCurrentScreen: Action()
    data class LoadData(val dataLoader: Observable<Unit>): Action()
    object ShowToast : Action()
    object ShowDialog: Action()
    data class AddNote(val note: Note): Action()
    object CloseDialog: Action()

}