package com.anaykamat.examples.android.tdd.screens

import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.anaykamat.examples.android.tdd.BuildConfig
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.views.SplashView

/**
 * Created by anay on 08/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class SplashScreenTest {

    private fun screen(): SplashScreen = SplashScreen()

    @Test
    fun itShouldBuildAndProvideSplashView(){
        val splashView = screen().buildView(Robolectric.buildActivity(MainActivity::class.java).get()) as SplashView
        Assert.assertNotNull(splashView)
    }

}