package com.anaykamat.examples.android.tdd.kotlin_data.events

import com.anaykamat.examples.android.tdd.kotlin_data.models.Note

sealed class DeleteDialogEvents{
    data class OkClicked(val note:Note): DeleteDialogEvents()
    object CancelClicked: DeleteDialogEvents()
}
