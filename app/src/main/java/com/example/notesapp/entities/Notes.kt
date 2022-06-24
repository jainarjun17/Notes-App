package com.example.notesapp.entities

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
class Notes {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "title")
    var title: String?= null

    @ColumnInfo(name = "sub_title")
    var sub_title: String?= null

    @ColumnInfo(name = "data_time")
    var data_time: String?= null

    @ColumnInfo(name = "note_text")
    var note_text: String?= null

    @ColumnInfo(name = "img_path")
    var img_path: String?= null

    @ColumnInfo(name = "web_link")
    var web_link: String?= null

    @ColumnInfo(name = "color_info")
    var color_info: String?= null
}
