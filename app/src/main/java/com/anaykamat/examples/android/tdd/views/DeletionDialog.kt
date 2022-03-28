package com.anaykamat.examples.android.tdd.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.kotlin_data.events.DeleteDialogEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note

/**
 * Created by Kartik on 28/03/2022.
 */
class DeletionDialog : DialogFragment() {

    private val events: PublishSubject<DeleteDialogEvents> = PublishSubject.create()
    private lateinit var note: Note

    fun eventsObservable(): Observable<DeleteDialogEvents> = events.hide().share()

    fun setNote(note: Note) {
        this.note = note
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val dialog = AlertDialog.Builder(activity)
            .setTitle("Delete Note ?")
            .setMessage("Are you sure you wan to delete - ${note.note} ?")
            .setCancelable(false)
            .setPositiveButton(R.string.dialog_ok, { dialog, whichButton -> })
            .setNegativeButton(R.string.dialog_cancel, { dialog, whichButton -> })
            .create()

        dialog.setOnShowListener { dialog ->
            (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE)
                .setOnClickListener { view -> events.onNext(DeleteDialogEvents.OkClicked(note)) }
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setOnClickListener { view -> events.onNext(DeleteDialogEvents.CancelClicked) }

        }
        return dialog
    }
}