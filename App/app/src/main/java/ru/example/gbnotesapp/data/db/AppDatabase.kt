package ru.example.gbnotesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.data.model.Note

@Database(entities = [Note::class, Folder::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao
}
