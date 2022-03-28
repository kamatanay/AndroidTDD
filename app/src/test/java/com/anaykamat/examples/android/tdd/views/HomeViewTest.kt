package com.anaykamat.examples.android.tdd.views

import android.app.AlertDialog
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.MainActivity
import org.robolectric.Robolectric
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowDialog
import com.anaykamat.examples.android.tdd.kotlin_data.events.HomeViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.TimeUnit


/**
 * Created by anay on 09/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class HomeViewTest {

    companion object {
        private val SCREEN_HEIGHT: Int = 500
        private val SCREEN_WIDTH: Int = 200

    }

    private fun view(): HomeView {
        return Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .visible()
            .get().let { HomeView(it) }
    }

    @Test
    fun itShouldMatchTheHeightAndWidthOfParent() {
        val view = view()
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, view.layoutParams.width)
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, view.layoutParams.height)
    }

    @Test
    fun itShouldHaveListViewOccupyingEntireSpace() {
        val view = view()
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        val listView = view.findViewById<ListView>(R.id.list_view)

        Assert.assertEquals(SCREEN_WIDTH, listView.width)
        Assert.assertEquals(SCREEN_HEIGHT, listView.height)
    }

    @Test
    fun itShouldHaveButtonHolderInLowerRightScreenCorner() {
        val view = view()

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        val linearLayoutView = view.findViewById<LinearLayout>(R.id.button_holder)

        Assert.assertEquals(SCREEN_WIDTH - linearLayoutView.width - 16, linearLayoutView.x.toInt())
        Assert.assertEquals(SCREEN_HEIGHT - linearLayoutView.height - 16,
            linearLayoutView.y.toInt())
    }

    @Test
    fun itShouldHaveButtonHolderThatContainsAddAndDeleteButton() {
        val view = view()

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)


        val linearLayoutView = view.findViewById<LinearLayout>(R.id.button_holder)
        val addButtonView = view.findViewById<FloatingActionButton>(R.id.add_button_view)
        val deleteButtonView = view.findViewById<FloatingActionButton>(R.id.delete_button_view)

        Assert.assertEquals(addButtonView, linearLayoutView.getChildAt(0))
        Assert.assertEquals(deleteButtonView, linearLayoutView.getChildAt(1))
    }


    //Test failing because of view change, need to modify
    @Test
    fun itShouldHaveFloatingActionButtonInLowerRightScreenCorner() {
        val view = view()

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)


        val addButtonView = view.findViewById<FloatingActionButton>(R.id.add_button_view)

        Assert.assertEquals(SCREEN_WIDTH - addButtonView.width - 16, addButtonView.x.toInt())
        Assert.assertEquals(SCREEN_HEIGHT - addButtonView.height - 16, addButtonView.y.toInt())
    }

    @Test
    fun itShouldRaiseTheEventWhenAddButtonIsClicked() {
        val view = view()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (event !== HomeViewEvents.AddButtonClicked) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            view.findViewById<FloatingActionButton>(R.id.add_button_view).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun itShouldRaiseTheEventWhenDeleteButtonIsClicked() {
        val view = view()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (event !== HomeViewEvents.DeleteButtonCLicked) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            view.findViewById<FloatingActionButton>(R.id.delete_button_view).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun itShouldAllowAddingNewNoteToListView() {
        val view = view()

        val noteText = "Do this"
        val note = Note(noteText)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        view.add(note)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(noteText,
            (view.findViewById<ListView>(R.id.list_view).getChildAt(0) as TextView).text)
    }

    @Test
    fun itShouldAllowDeletingNoteFromListView() {
        val view = view()

        val noteText = "Do this"
        val note = Note(noteText)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        view.add(note)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

//        Assert.assertEquals(noteText,
//            (view.findViewById<ListView>(R.id.list_view).getChildAt(0) as TextView).text)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        view.deleteNote(note)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(0,
            (view.findViewById<ListView>(R.id.list_view).childCount))
    }

    @Test
    fun itShouldShowTheDialog() {
        val view = view()
        view.showDialogForNewTodo(view.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        val dialog = ShadowDialog.getLatestDialog()
        val title = Shadows.shadowOf(dialog as AlertDialog).title.toString()

        Assert.assertEquals("Add Todo", title)
        Assert.assertTrue(dialog.isShowing)
    }

    @Test
    fun itShouldShowDeleteNoteDialog() {
        val view = view()
        view.showDialogForNoteDeletion(view.context as MainActivity,Note("Hi"))
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        val dialog = ShadowDialog.getLatestDialog()
        val title = Shadows.shadowOf(dialog as AlertDialog).title.toString()

        Assert.assertEquals("Delete Note ?", title)
        Assert.assertTrue(dialog.isShowing)
    }

    @Test
    fun itShouldAllowToDismissTheDialog() {
        val view = view()
        view.showDialogForNewTodo(view.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        view.dismissDialog()
        val dialog = ShadowDialog.getLatestDialog()
        Assert.assertTrue(Shadows.shadowOf(dialog).hasBeenDismissed())
    }

    @Test
    fun itShouldAllowToDismissNoteDeletionDialog() {
        val view = view()
        view.showDialogForNoteDeletion(view.context as MainActivity,Note("Hi"))
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        view.dismissDeletionDialog()
        val dialog = ShadowDialog.getLatestDialog()
        Assert.assertTrue(Shadows.shadowOf(dialog).hasBeenDismissed())
    }

    @Test
    fun itShouldRaiseTheEventOnSubmissionOfNote() {
        val view = view()
        view.showDialogForNewTodo(view.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (!(event is HomeViewEvents.NoteSubmitted)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            val dialog = ShadowDialog.getLatestDialog()
            (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()

    }

    @Test
    fun itShouldRaiseTheEventOnDeletionOfNote() {
        val view = view()
        view.showDialogForNoteDeletion(view.context as MainActivity,Note("Hi"))
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (!(event is HomeViewEvents.NoteDeleted)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            val dialog = ShadowDialog.getLatestDialog()
            (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()

    }

    @Test
    fun itShouldRaiseTheEventOnCancellationOfNoteDialog() {
        val view = view()
        view.showDialogForNewTodo(view.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (!(event is HomeViewEvents.DialogDismissed)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            val dialog = ShadowDialog.getLatestDialog()
            (dialog as AlertDialog).getButton(Dialog.BUTTON_NEGATIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()

    }

    @Test
    fun itShouldRaiseTheEventOnCancellationOfDeletionDialog() {
        val view = view()
        view.showDialogForNoteDeletion(view.context as MainActivity, Note("Hi"))
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (!(event is HomeViewEvents.DeletionDialogDismissed)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            val dialog = ShadowDialog.getLatestDialog()
            (dialog as AlertDialog).getButton(Dialog.BUTTON_NEGATIVE).performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()

    }
}