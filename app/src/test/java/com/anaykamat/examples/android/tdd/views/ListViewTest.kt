package com.anaykamat.examples.android.tdd.views

import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import junit.framework.Assert
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note

/**
 * Created by anay on 09/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class ListViewTest {

    companion object {
        private val SCREEN_HEIGHT: Int = 500
        private val SCREEN_WIDTH: Int = 200

    }

    private fun view(): ListView =
        ListView(RuntimeEnvironment.application.applicationContext).also {
            it.layoutParams = ViewGroup.LayoutParams(SCREEN_WIDTH, SCREEN_HEIGHT)
        }

    @Test
    fun itShouldInitialiseWithEmptyItems() {
        val view = view()
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(0, view.childCount)
    }

    @Test
    fun itShouldVerifyGiveColorToTextView() {
        val view = view()
        val noteText = "Do this"
        val note = Note(noteText)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        val textView = view.getChildAt(0) as TextView

        Assert.assertEquals(1, view.childCount)
        Assert.assertNotNull(textView)
        Assert.assertEquals(ContextCompat.getColor(view.context, android.R.color.holo_blue_light),
            textView.currentTextColor)
    }

    @Test
    fun itShouldVerifyGiveTextSizeToTextView() {
        val view = view()
        val noteText = "Do this"
        val note = Note(noteText)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        val textView = view.getChildAt(0) as TextView

        Assert.assertEquals(1, view.childCount)
        Assert.assertNotNull(textView)
        Assert.assertEquals(24f, textView.textSize)
    }

    @Test
    fun itShouldVerifyGiveTextPaddingSetToTextView() {
        val view = view()
        val noteText = "Do this"
        val note = Note(noteText)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        val textView = view.getChildAt(0) as TextView

        Assert.assertEquals(1, view.childCount)
        Assert.assertNotNull(textView)
        Assert.assertEquals(100, textView.paddingLeft)
        Assert.assertEquals(100, textView.paddingRight)
        Assert.assertEquals(50, textView.paddingTop)
        Assert.assertEquals(50, textView.paddingBottom)
    }

    @Test
    fun itShouldAddAGivenNoteAsChild() {
        val noteText = "Do this"
        val note = Note(noteText)

        val view = view()
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(1, view.childCount)
        Assert.assertEquals(noteText, (view.getChildAt(0) as TextView).text)
    }

    @Test
    fun itShouldDeleteAGivenNoteAsChild() {
        val noteText = "Do this"
        val note = Note(noteText)

        val view = view()
        //Add Note First
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.add(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(1, view.childCount)
        Assert.assertEquals(noteText, (view.getChildAt(0) as TextView).text)

        //Delete the added Note
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)
        view.delete(note)
        view.measure(View.MeasureSpec.makeMeasureSpec(SCREEN_WIDTH, EXACTLY),
            View.MeasureSpec.makeMeasureSpec(SCREEN_HEIGHT, EXACTLY))
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)

        Assert.assertEquals(0, view.childCount)
    }

}