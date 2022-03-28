package com.anaykamat.examples.android.tdd.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anaykamat.examples.android.tdd.kotlin_data.events.HomeViewEvents
import com.anaykamat.examples.android.tdd.kotlin_data.models.Note
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by anay on 09/08/18.
 */
class ListView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes)


    private val listItems: ArrayList<Note> = ArrayList()
    private val events: PublishSubject<HomeViewEvents> = PublishSubject.create()

    fun eventsObservable(): Observable<HomeViewEvents> = events.hide().share()

    private class ViewHolder(
        private val view: View,
        private val events: PublishSubject<HomeViewEvents>
    ) : RecyclerView.ViewHolder(view) {

        fun update(note: Note) {
            val tView = view as TextView
            tView.text = note.note
        }

    }

    private val viewAdapter = object : RecyclerView.Adapter<ViewHolder>() {

        override fun getItemCount() = listItems.count()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            listItems[position].let {
                holder.update(it)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = TextView(parent.context)
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            view.setTextColor(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            view.setPadding(100, 50, 100, 50)
            return ViewHolder(view, events)
        }

        fun add(content: Note) {
            listItems += content
            notifyDataSetChanged()
        }

        fun deleteNote(content: Note) {
            listItems.remove(content)
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

    fun delete(note: Note) {
        viewAdapter.deleteNote(note)
    }


}