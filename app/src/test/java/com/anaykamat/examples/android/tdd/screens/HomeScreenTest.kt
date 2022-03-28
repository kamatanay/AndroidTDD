package com.anaykamat.examples.android.tdd.screens

import android.app.AlertDialog
import android.app.Dialog
import io.reactivex.Observable
import junit.framework.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowDialog
import com.anaykamat.examples.android.tdd.BuildConfig
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.kotlin_data.events.HomeViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.anaykamat.examples.android.tdd.screens.home.HomeReducer
import com.anaykamat.examples.android.tdd.screens.home.HomeViewUpdater
import com.anaykamat.examples.android.tdd.views.HomeView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.robolectric.shadows.ShadowLooper
import java.util.concurrent.TimeUnit

/**
 * Created by anay on 09/08/18.
 */
@RunWith(RobolectricTestRunner::class)
class HomeScreenTest {

    private fun screen(): HomeScreen = HomeScreen()

    @Test
    fun itShouldBuildAndProvideHomeView() {
        val homeView = screen().buildView(Robolectric.buildActivity(MainActivity::class.java)
            .get()) as HomeView
        Assert.assertNotNull(homeView)
    }


    @Test
    fun itShouldBuildProviderHomeViewAndEmitDialogCancelledEventOnCancellingDialog() {
        val homeView = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .visible()
            .get().let { screen().buildView(it) as HomeView }
        Assert.assertNotNull(homeView)
        homeView.showDialogForNewTodo(homeView.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()
        Observable.create<HomeViewEvents.DialogDismissed> { emitter ->
            homeView.eventsObservable().subscribe {
                emitter.onNext(it as HomeViewEvents.DialogDismissed)
            }

            ShadowDialog.getLatestDialog()?.let { it as? AlertDialog? }
                ?.let { it.getButton(Dialog.BUTTON_NEGATIVE).performClick() }
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun itShouldBuildProviderHomeViewAndEmitNoteSubmittedEventOnDialogPositiveBtnClick() {
        val homeView = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .visible()
            .get().let { screen().buildView(it) as HomeView }
        Assert.assertNotNull(homeView)
        homeView.showDialogForNewTodo(homeView.context as MainActivity)
        ShadowLooper.shadowMainLooper().runToEndOfTasks()
        Observable.create<HomeViewEvents.NoteSubmitted> { emitter ->
            homeView.eventsObservable().subscribe {
                emitter.onNext(it as HomeViewEvents.NoteSubmitted)
            }

            ShadowDialog.getLatestDialog()?.let { it as? AlertDialog? }
                ?.let { it.getButton(Dialog.BUTTON_POSITIVE).performClick() }
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }


    @Test
    fun itShouldBuildProviderHomeViewAndEmitDeletionDialogCancelledEventOnCancellingDeletionDialog() {
        val homeView = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .visible()
            .get().let { screen().buildView(it) as HomeView }
        Assert.assertNotNull(homeView)
        homeView.showDialogForNoteDeletion(homeView.context as MainActivity, Note("Hi"))
        ShadowLooper.shadowMainLooper().runToEndOfTasks()
        Observable.create<HomeViewEvents.DeletionDialogDismissed> { emitter ->
            homeView.eventsObservable().subscribe {
                emitter.onNext(it as HomeViewEvents.DeletionDialogDismissed)
            }

            ShadowDialog.getLatestDialog()?.let { it as? AlertDialog? }
                ?.let { it.getButton(Dialog.BUTTON_NEGATIVE).performClick() }
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }

    @Test
    fun itShouldBuildProviderHomeViewAndEmitNoteDeletedEventOnDeletionDialogPositiveBtnClick() {
        val homeView = Robolectric.buildActivity(MainActivity::class.java)
            .create()
            .start()
            .resume()
            .visible()
            .get().let { screen().buildView(it) as HomeView }
        Assert.assertNotNull(homeView)
        homeView.showDialogForNoteDeletion(homeView.context as MainActivity, Note("Hi"))
        ShadowLooper.shadowMainLooper().runToEndOfTasks()
        Observable.create<HomeViewEvents.NoteDeleted> { emitter ->
            homeView.eventsObservable().subscribe {
                emitter.onNext(it as HomeViewEvents.NoteDeleted)
            }

            ShadowDialog.getLatestDialog()?.let { it as? AlertDialog? }
                ?.let { it.getButton(Dialog.BUTTON_POSITIVE).performClick() }
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
    }


    @Test
    fun itShouldBuildAndProvideHomeViewAddButtonClickedEvent() {
        val homeView = screen().buildView(Robolectric.buildActivity(MainActivity::class.java)
            .get()) as HomeView
        val button = homeView.findViewById<FloatingActionButton>(R.id.add_button_view)
        Observable.create<HomeViewEvents.AddButtonClicked> { emitter ->
            homeView.eventsObservable().subscribe {
                emitter.onNext(it as HomeViewEvents.AddButtonClicked)
            }

            button.performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
        Assert.assertNotNull(homeView)
    }

    @Test
    fun itShouldBuildAndProvideHomeViewDeleteButtonClickedEvent() {
        val homeView = screen().buildView(Robolectric.buildActivity(MainActivity::class.java)
            .get()) as HomeView
        val button = homeView.findViewById<FloatingActionButton>(R.id.delete_button_view)
        Observable.create<HomeViewEvents.DeleteButtonCLicked> { emitter ->
            homeView.eventsObservable().subscribe {
                emitter.onNext(it as HomeViewEvents.DeleteButtonCLicked)
            }

            button.performClick()
        }.timeout(5, TimeUnit.SECONDS).blockingFirst()
        Assert.assertNotNull(homeView)
    }

    @Test
    fun itShouldProvideReducer() {
        Assert.assertTrue(screen().reducer() is HomeReducer)
    }

    @Test
    fun itShouldProvideUpdater() {
        Assert.assertTrue(screen().updater() is HomeViewUpdater)
    }


}