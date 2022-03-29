package com.anaykamat.examples.android.tdd.views

import android.app.AlertDialog
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
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
    fun itShouldMatchTheHeightAndWidthOfParent(){
        val view = view()
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, view.layoutParams.width)
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, view.layoutParams.height)
    }

    @Test
    fun itShouldHaveListViewOccupyingEntireSpace(){
        val view = view()
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)

        val listView = view.findViewById<ListView>(R.id.list_view)

        Assert.assertEquals(SCREEN_WIDTH, listView.width)
        Assert.assertEquals(SCREEN_HEIGHT, listView.height)
    }

    @Test
    fun itShouldHaveFloatingActionButtonInLowerRightScreenCorner(){
        val view = view()

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)

        val addButtonView = view.findViewById<FloatingActionButton>(R.id.add_button_view)

        Assert.assertEquals(SCREEN_WIDTH -addButtonView.width - 16, addButtonView.x.toInt())
        Assert.assertEquals(SCREEN_HEIGHT -addButtonView.height - 16, addButtonView.y.toInt())
    }

    @Test
    fun itShouldRaiseTheEventWhenAddButtonIsClicked(){
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
    fun itShouldAllowAddingNewNoteToListView(){
        val view = view()

        val noteText = "Do this"
        val note = Note(noteText)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)

        view.add(note)

        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(noteText, (view.findViewById<ListView>(R.id.list_view).getChildAt(0) as NoteView).text)
    }

    @Test
    fun itShouldShowTheDialog(){
        val view = view()
        view.showDialogForNewTodo(view.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        val dialog = ShadowDialog.getLatestDialog()
        val title = Shadows.shadowOf(dialog as AlertDialog).title.toString()

        Assert.assertEquals("Add Todo", title)
        Assert.assertTrue(dialog.isShowing)
    }

    @Test
    fun itShouldAllowToDismissTheDialog(){
        val view = view()
        view.showDialogForNewTodo(view.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        view.dismissDialog()
        val dialog = ShadowDialog.getLatestDialog()
        Assert.assertTrue(Shadows.shadowOf(dialog).hasBeenDismissed())
    }

    @Test
    fun itShouldRaiseTheEventOnSubmissionOfNote(){
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
    fun itShouldRaiseTheEventOnCancellationOfNoteDialog(){
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
}