package com.example.notesapp.Adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.util.TimeUnit.values
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.entities.Notes
import kotlinx.android.synthetic.main.item_display_notes.view.*
import java.time.chrono.JapaneseEra.values

class RecyclerViewAdapter: RecyclerView.Adapter<NoteViewHolder>() {
    var notesList:List<Notes>?=null
    var listener: OnItemClickListener? = null

    fun setList(list:List<Notes>?){
        notesList=list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {


        var myView=LayoutInflater.from(parent.context).inflate(R.layout.item_display_notes,parent,false)

        return NoteViewHolder(myView)
    }

    fun setOnClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.itemView.titleText.text=notesList!![position].title.toString()
        holder.itemView.subTitleText.text= notesList!![position].sub_title.toString()
        holder.itemView.dataTimeText.text=notesList!![position].data_time.toString()
        if(notesList!![position].img_path!=null) {
            holder.itemView.imageView.setImageBitmap(BitmapFactory.decodeFile(notesList!![position].img_path))
        }

        if(notesList!![position].color_info!=null) {
            holder.itemView.cardView.setCardBackgroundColor(notesList!![position].color_info!!.toInt())
        }
        else {
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor("#171C26"))
        }
        //id=notesList!![position].id
        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(notesList!![position].id!!)
        }

    }


    override fun getItemCount(): Int {
        return notesList!!.size
    }

    interface OnItemClickListener{
        fun onClicked(noteId:Int)
    }


}

class NoteViewHolder(view: View):RecyclerView.ViewHolder(view) {

}