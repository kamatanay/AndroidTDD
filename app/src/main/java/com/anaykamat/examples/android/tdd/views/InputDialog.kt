package com.anaykamat.examples.android.tdd.views

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.kotlin_data.events.DialogEvents

/**
 * Created by anay on 09/08/18.
 */
class InputDialog:DialogFragment() {

    private val events:PublishSubject<DialogEvents> = PublishSubject.create()

    fun eventsObservable():Observable<DialogEvents> = events.hide().share()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val editText = EditText(activity).also {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        }
        val dialog = AlertDialog.Builder(activity)
                .setTitle("Add Todo")
                .setView(editText)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_ok, { dialog, whichButton -> })
                .setNegativeButton(R.string.dialog_cancel, { dialog, whichButton -> })
                .create()

        dialog.setOnShowListener { dialog ->
            (dialog as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { view -> events.onNext(DialogEvents.OkClicked(editText.text.toString())) }
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener { view -> events.onNext(DialogEvents.CancelClicked) }

        }
        return dialog
    }
}