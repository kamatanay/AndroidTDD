package com.anaykamat.examples.android.tdd.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Looper
import android.view.ViewGroup
import android.widget.EditText
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.BuildConfig
import org.robolectric.Robolectric
import org.robolectric.Shadows
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.kotlin_data.events.DeleteDialogEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.DialogEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.TimeUnit


/**
 * Created by Kartik on 28/03/2022.
 */
@RunWith(RobolectricTestRunner::class)
class DeletionDialogTest {

    private fun dialog(): DeletionDialog {
        return DeletionDialog().also { dialog ->
            Robolectric.setupActivity(MainActivity::class.java).supportFragmentManager.let { fragmentManager ->
                dialog.also { dialog ->
                    dialog.setNote(Note("Hi"))
                    dialog.show(fragmentManager, "dialog")
                    ShadowLooper.shadowMainLooper().runToEndOfTasks()
                }
            }
        }
    }


    @Test
    fun itShouldHaveDeleteNoteAsTitle() {
        val dialog = dialog()
        val title = Shadows.shadowOf(dialog.dialog).title
        Assert.assertEquals("Delete Note ?", title)
    }

    @Test
    fun itShouldHaveNoteTextAsPartOfMessage() {
        val dialog = dialog()
        val view = Shadows.shadowOf(dialog.dialog as AlertDialog).message
        Assert.assertTrue(view.contains("Hi"))
    }


    @Test
    fun itShouldHaveButtonToDeleteNote() {
        val dialog = dialog()
        val button = (dialog.dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        Assert.assertNotNull(button)
        Assert.assertEquals("Ok", button.text)
    }

    @Test
    fun itShouldHaveButtonToCancelDialog() {
        val dialog = dialog()
        val button = (dialog.dialog as AlertDialog).getButton(Dialog.BUTTON_NEGATIVE)
        Assert.assertNotNull(button)
        Assert.assertEquals("Cancel", button.text)
    }


    @Test
    fun itShouldRaiseEventWithNoteContentOnClickingOkButton() {
        val dialog = dialog()

        Observable.create<Unit> { emitter ->
            dialog.eventsObservable().subscribe { event ->
                if (!(event is DeleteDialogEvents.OkClicked) || event.note != Note("Hi")) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            (dialog.dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun itShouldRaiseEventOnClickOfCancelButton() {
        val dialog = dialog()

        Observable.create<Unit> { emitter ->
            dialog.eventsObservable().subscribe { event ->
                if (!(event is DeleteDialogEvents.CancelClicked)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            val alertDialog = (dialog.dialog as AlertDialog)
            alertDialog.getButton(Dialog.BUTTON_NEGATIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

}