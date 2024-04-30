package ru.example.gbnotesapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    var isSelected: Boolean = false
): Parcelable
