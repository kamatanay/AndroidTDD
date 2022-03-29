package com.anaykamat.examples.android.tdd.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anaykamat.examples.android.tdd.R
import com.anaykamat.examples.android.tdd.kotlin_data.events.ListViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by anay on 09/08/18.
 */
class ListView: RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)

    private val listItems: ArrayList<Note> = ArrayList()

    private val events: PublishSubject<ListViewEvents> = PublishSubject.create()

    fun eventsObservable(): Observable<ListViewEvents> = events.hide().share()

    private class ViewHolder(private val view: View, val events: PublishSubject<ListViewEvents>):RecyclerView.ViewHolder(view) {
        fun update(note: Note, position: Int){
            (view as NoteView).text = note.note
            (view as NoteView).eventsObservable().subscribe{
                events.onNext(ListViewEvents.RemoveNote(position = position, note = note.note))
            }
        }

    }
    private val viewAdapter = object:RecyclerView.Adapter<ViewHolder>(){

        override fun getItemCount() = listItems.count()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            listItems[position]?.let {
                holder?.update(it, position)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = NoteView(parent.context)
            view.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            view.setPadding(100,50,100,50)
            return ViewHolder(view, events)
        }

        fun add(content: Note) {
            listItems += content
            notifyDataSetChanged()
        }

        fun removeAt(position: Int){
            listItems.removeAt(position)
            notifyDataSetChanged()

        }

    }

    init {
        this.layoutManager = LinearLayoutManager(context)
        this.adapter = viewAdapter
    }

    fun add(note: Note) {
        viewAdapter.add(note)
    }

    fun removeNoteAt(position: Int) {
        viewAdapter.removeAt(position)
    }

}