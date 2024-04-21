package ru.example.gbnotesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val folderId: Int,
    val title: String,
    val content: String,
    val creationDate: String
): Parcelable
