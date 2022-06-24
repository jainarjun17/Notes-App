package com.example.notesapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.Dao.NotesDao
import com.example.notesapp.entities.Notes

@Database(entities = [Notes::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun getNotesDao():NotesDao
    companion object{
         private var INSTANCE:NoteDatabase?=null

        fun getNotesDatabase(context: Context):NoteDatabase{
            if(INSTANCE==null){
                synchronized(this){
                    INSTANCE= Room.databaseBuilder(context,NoteDatabase::class.java,"NotesDatabase").build()
                }
            }
            return INSTANCE!!
        }
    }
 }