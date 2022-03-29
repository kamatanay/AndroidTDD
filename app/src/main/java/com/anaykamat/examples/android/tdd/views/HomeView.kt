package com.anaykamat.examples.android.tdd.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.kotlin_data.events.DialogEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.HomeViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.ListViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.view_home.view.*

/**
 * Created by anay on 09/08/18.
 */
open class HomeView: LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    private val inputDialog: InputDialog = InputDialog()

    private val events: PublishSubject<HomeViewEvents> = PublishSubject.create()

    fun eventsObservable(): Observable<HomeViewEvents> = events.hide().share()

    init{
        this.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        android.view.LayoutInflater.from(context).inflate(R.layout.view_home, this, true)

        findViewById<FloatingActionButton>(R.id.add_button_view).setOnClickListener {
            events.onNext(HomeViewEvents.AddButtonClicked)
        }

        inputDialog.eventsObservable().subscribe { event ->
            when(event){
                is DialogEvents.OkClicked -> events.onNext(HomeViewEvents.NoteSubmitted(Note(event.note)))
                is DialogEvents.CancelClicked -> events.onNext(HomeViewEvents.DialogDismissed)
            }
        }

        findViewById<ListView>(R.id.list_view).eventsObservable().subscribe {
            when(it){
                is ListViewEvents.RemoveNote -> events.onNext(HomeViewEvents.RemoveNote(it.position))
            }
        }

    }

    open fun add(note: Note){
        findViewById<ListView>(R.id.list_view).add(note)
    }

    open fun showDialogForNewTodo(mainActivity: MainActivity) {
        inputDialog.show(mainActivity.supportFragmentManager, "Dialog")
    }

    open fun dismissDialog() {
        inputDialog.dismiss()
    }

    open fun removeNoteAt(position: Int) {
        findViewById<ListView>(R.id.list_view).removeNoteAt(position)
    }
}