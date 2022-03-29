package com.anaykamat.examples.android.tdd.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.anaykamat.examples.android.tdd.MainActivity
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.kotlin_data.events.DialogEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.HomeViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.events.NoteViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Created by anay on 09/08/18.
 */
open class NoteView: LinearLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)


    private val events: PublishSubject<NoteViewEvents> = PublishSubject.create()

    fun eventsObservable(): Observable<NoteViewEvents> = events.hide().share()

    init{
        this.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        TextView(context).also {
            it.tag = "Text"
            it.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            addView(it)
        }

        Button(context).also{
           it.layoutParams = LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
           it.tag = "Remove"
           it.text = "Remove"
            it.setOnClickListener {
                events.onNext(NoteViewEvents.RemoveClicked)
            }
        }.let(::addView)
    }

    var text:String
        get() = findViewWithTag<TextView>("Text").text.toString()
        set(value) {
            findViewWithTag<TextView>("Text").text = value
        }

}