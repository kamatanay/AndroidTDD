package com.anaykamat.examples.android.tdd.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.kotlin_data.events.DeleteDialogEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.DialogEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.HomeViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by anay on 09/08/18.
 */
open class HomeView : LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    private val inputDialog: InputDialog = InputDialog()

    private val deletionDialog: DeletionDialog = DeletionDialog()

    private val events: PublishSubject<HomeViewEvents> = PublishSubject.create()

    fun eventsObservable(): Observable<HomeViewEvents> = events.hide().share()

    init {
        this.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        android.view.LayoutInflater.from(context).inflate(R.layout.view_home, this, true)

        findViewById<FloatingActionButton>(R.id.add_button_view).setOnClickListener {
            events.onNext(HomeViewEvents.AddButtonClicked)
        }
        findViewById<FloatingActionButton>(R.id.delete_button_view).setOnClickListener {
            events.onNext(HomeViewEvents.DeleteButtonCLicked)
        }

        inputDialog.eventsObservable().subscribe { event ->
            when (event) {
                is DialogEvents.OkClicked -> events.onNext(HomeViewEvents.NoteSubmitted(Note(event.note)))
                is DialogEvents.CancelClicked -> events.onNext(HomeViewEvents.DialogDismissed)
            }
        }
        deletionDialog.eventsObservable().subscribe { event ->
            when (event) {
                DeleteDialogEvents.CancelClicked -> events.onNext(HomeViewEvents.DeletionDialogDismissed)
                is DeleteDialogEvents.OkClicked -> events.onNext(HomeViewEvents.NoteDeleted(event.note))
            }
        }

    }

    open fun add(note: Note) {
        findViewById<ListView>(R.id.list_view).add(note)
    }

    open fun deleteNote(note: Note) {
        findViewById<ListView>(R.id.list_view).delete(note)
    }

    open fun showDialogForNewTodo(mainActivity: MainActivity) {
        inputDialog.show(mainActivity.supportFragmentManager, "Dialog")
    }

    open fun showDialogForNoteDeletion(mainActivity: MainActivity, note: Note) {
        deletionDialog.setNote(note)
        deletionDialog.show(mainActivity.supportFragmentManager, "Deletion-Dialog")

    }

    open fun dismissDialog() {
        inputDialog.dismiss()
    }

    open fun dismissDeletionDialog() {
        deletionDialog.dismiss()
    }
}