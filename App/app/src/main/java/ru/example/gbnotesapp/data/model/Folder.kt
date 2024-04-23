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
    val name: String, // Имя папки
    var noteCount: Int = 0, // количество заметок в папке
    var isSelected: Boolean = false // флаг, указывающий, выбрана ли папка для отображения
): Parcelable
