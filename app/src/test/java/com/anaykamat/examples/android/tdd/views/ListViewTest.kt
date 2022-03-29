package com.anaykamat.examples.android.tdd.views

import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.anaykamat.examples.android.tdd.kotlin_data.events.ListViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.NoteViewEvents
import junit.framework.Assert
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import io.reactivex.Observable
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.TimeUnit

/**
 * Created by anay on 09/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class ListViewTest {

    companion object {
        private val SCREEN_HEIGHT: Int = 500
        private val SCREEN_WIDTH: Int = 200

    }

    private fun view(): ListView = ListView(RuntimeEnvironment.application.applicationContext).also {
        it.layoutParams = ViewGroup.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT)
    }

    @Test
    fun itShouldInitialiseWithEmptyItems(){
        val view = view()
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(0, view.childCount)
    }


    @Test
    fun itShouldVerifyGiveTextPaddingSetToTextView() {
        val view = view()
        val noteText = "Do this"
        val note = Note(noteText)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)
        val textView = view.getChildAt(0) as NoteView

        Assert.assertEquals(1, view.childCount)
        Assert.assertNotNull(textView)
        Assert.assertEquals(100, textView.paddingLeft)
        Assert.assertEquals(100, textView.paddingRight)
        Assert.assertEquals(50, textView.paddingTop)
        Assert.assertEquals(50, textView.paddingBottom)
    }

    @Test
    fun itShouldAddAGivenNoteAsChild(){
        val noteText = "Do this"
        val note = Note(noteText)

        val view = view()
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(1, view.childCount)
        Assert.assertEquals(noteText, (view.getChildAt(0) as NoteView).text)
    }

    @Test
    fun itShouldRaiseTheEventOnClickOfRemoveButton(){
        val noteText = "Do this"
        val note = Note(noteText)

        val view = view()
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY), View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0,0, SCREEN_WIDTH, SCREEN_HEIGHT)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (event != ListViewEvents.RemoveNote(position = 0, note = noteText)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            (view.getChildAt(0) as NoteView).findViewWithTag<Button>("Remove").performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()

    }

    @Test
    fun itShouldRaiseTheEventOnClickOfRemoveButtonAlongWithPositionAndTextValueOfClickedNote(){
        val note = Note("Do this")

        val noteText = "Do this again"
        val noteAgain = Note(noteText)

        val view = view()
        view.add(note)
        view.add(noteAgain)
        view.measure(0,0)
        view.layout(0,0, SCREEN_WIDTH, 5000)
        view.measure(0,0)
        view.layout(0,0, SCREEN_WIDTH, 5000)


        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (event != ListViewEvents.RemoveNote(position = 1, note = noteText)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            (view.getChildAt(1) as NoteView).findViewWithTag<Button>("Remove").performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()

    }

}