package com.anaykamat.examples.android.tdd.views

import android.app.AlertDialog
import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.anaykamat.examples.android.tdd.kotlin_data.events.NoteViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.TimeUnit


/**
 * Created by anay on 09/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class NoteViewTest {

    companion object {
        private val SCREEN_HEIGHT: Int = 500
        private val SCREEN_WIDTH: Int = 200

    }

    private fun view(): NoteView {
        return Robolectric.buildActivity(MainActivity::class.java)
                .create()
                .start()
                .resume()
                .visible()
                .get().let { NoteView(it) }
    }

    @Test
    fun itShouldVerifyGiveColorToTextView() {
        val view = view()
        view.text = "Do this"
        view.measure(View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, NoteViewTest.SCREEN_WIDTH, NoteViewTest.SCREEN_HEIGHT)
        val textView = view.getChildAt(0) as TextView

        Assert.assertNotNull(textView)
        Assert.assertEquals(ContextCompat.getColor(view.context, android.R.color.holo_blue_light), textView.currentTextColor)
    }

    @Test
    fun itShouldVerifyGiveTextSizeToTextView() {
        val view = view()
        view.text = "Do this"
        view.measure(View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, NoteViewTest.SCREEN_WIDTH, NoteViewTest.SCREEN_HEIGHT)
        val textView = view.getChildAt(0) as TextView

        Assert.assertNotNull(textView)
        Assert.assertEquals(24f, textView.textSize)
    }

    @Test
    fun itShouldHaveAButtonToRemoveTheNote() {
        val view = view()
        view.text = "Do this"
        view.measure(View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, NoteViewTest.SCREEN_WIDTH, NoteViewTest.SCREEN_HEIGHT)

        val button = view.getChildAt(1) as Button

        Assert.assertNotNull(button)
        Assert.assertEquals("Remove", button.text)
        Assert.assertEquals("Remove", button.tag)
    }

    @Test
    fun itShouldRaiseTheEventOnClickOfRemoveButton(){
        val view = view()
        view.text = "Do this"
        view.measure(View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_WIDTH, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(NoteViewTest.SCREEN_HEIGHT, View.MeasureSpec.EXACTLY))
        view.layout(0,0, NoteViewTest.SCREEN_WIDTH, NoteViewTest.SCREEN_HEIGHT)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()

        Observable.create<Unit> { emitter ->
            view.eventsObservable().subscribe { event ->
                if (!(event is NoteViewEvents.RemoveClicked)) return@subscribe
                emitter.onNext(Unit)
                emitter.onComplete()
            }
            view.findViewWithTag<Button>("Remove").performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()

    }
}