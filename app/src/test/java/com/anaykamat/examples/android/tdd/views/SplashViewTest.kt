package com.anaykamat.examples.android.tdd.views

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.FIND_VIEWS_WITH_TEXT
import android.view.ViewGroup
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.BuildConfig

/**
 * Created by anay on 07/08/18.
 */

@RunWith(RobolectricTestRunner::class)
class SplashViewTest {

    fun view(): SplashView = SplashView(RuntimeEnvironment.application.applicationContext, null)

    @Test
    fun itShouldMatchParentForHeightAndWidth() {
        val view = view()
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, view.layoutParams.width)
        Assert.assertEquals(ViewGroup.LayoutParams.MATCH_PARENT, view.layoutParams.height)
    }

    @Test
    fun itShouldShowRequestUserToWait() {
        val view = view()
        val pleaseWaitViews = ArrayList<View>()
        view.findViewsWithText(pleaseWaitViews, "Please Wait", FIND_VIEWS_WITH_TEXT)
        Assert.assertEquals(1, pleaseWaitViews.count())
    }

    @Test
    fun itShouldMatchWithGivenBackgroundColor() {
        val expectedBackgroundColor: Int = 0x0099cc
        val view = view()
        val backgroundColor = (view.background as ColorDrawable).color
        assertEquals(expectedBackgroundColor, backgroundColor)
    }
}