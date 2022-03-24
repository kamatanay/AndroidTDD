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
import com.anaykamat.examples.android.tdd.kotlin_data.events.DialogEvents
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.TimeUnit


/**
 * Created by anay on 09/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class InputDialogTest {

    private fun dialog(): InputDialog {
        return InputDialog().also { dialog ->
            Robolectric.setupActivity(MainActivity::class.java).let{
                it.supportFragmentManager
            }.let{ fragmentManager ->
                dialog.also { dialog ->
                    dialog.show(fragmentManager,"dialog")
                    ShadowLooper.shadowMainLooper().runToEndOfTasks()
                }
            }
        }
    }

    @Test
    fun itShouldMatchInputViewForHeightAndWidth() {
        val dialog = dialog()
        val editText = Shadows.shadowOf(dialog.dialog as AlertDialog).view
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, editText.layoutParams.width)
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, editText.layoutParams.height)
    }

    @Test
    fun itShouldHaveAddTodoAsTitle(){
        val dialog = dialog()
        val title = Shadows.shadowOf(dialog.dialog).title
        Assert.assertEquals("Add Todo", title)
    }

    @Test
    fun itShouldHaveButtonToAddNote(){
        val dialog = dialog()
        val button = (dialog.dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        Assert.assertNotNull(button)
        Assert.assertEquals("Ok", button.text)
    }

    @Test
    fun itShouldHaveButtonToCancelDialog(){
        val dialog = dialog()
        val button = (dialog.dialog as AlertDialog).getButton(Dialog.BUTTON_NEGATIVE)
        Assert.assertNotNull(button)
        Assert.assertEquals("Cancel", button.text)
    }

    @Test
    fun itShouldHaveTextFieldToGetTextOfNote(){
        val dialog = dialog()
        val view = Shadows.shadowOf(dialog.dialog as AlertDialog).view
        Assert.assertTrue(view is EditText)
    }

    @Test
    fun itShouldRaiseEventWithNoteContentOnClickingOkButton(){
        val dialog = dialog()

        Observable.create<Unit> { emitter ->
            dialog.eventsObservable().subscribe { event ->
                if (!(event is DialogEvents.OkClicked) || event.note != "First TODO") return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            val alertDialog = (dialog.dialog as AlertDialog)
            Shadows.shadowOf(alertDialog).also { dialog ->
                dialog.view?.let { (it as EditText) }?.let { it.setText("First TODO") }
            }
            alertDialog.getButton(Dialog.BUTTON_POSITIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun itShouldRaiseEventOnClickOfCancelButton(){
        val dialog = dialog()

        Observable.create<Unit> { emitter ->
            dialog.eventsObservable().subscribe { event ->
                if (!(event is DialogEvents.CancelClicked)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            val alertDialog = (dialog.dialog as AlertDialog)
            alertDialog.getButton(Dialog.BUTTON_NEGATIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

}