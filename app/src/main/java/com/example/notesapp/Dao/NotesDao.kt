package com.example.notesapp.Dao

import androidx.room.*
import com.example.notesapp.entities.Notes

@Dao
interface NotesDao {
    @Insert
    suspend fun insertNote(notes: Notes)

    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun deleteNote(id:Int)

    @Query("SELECT * FROM Notes")
    suspend fun getAllNote():List<Notes>

    @Query("SELECT * FROM Notes WHERE id=:id")
    suspend fun getNote(id:Int):Notes

    @Update
    suspend fun updateNote(notes:Notes)

    @Delete
    suspend fun delete(notes: Notes)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNote()



}