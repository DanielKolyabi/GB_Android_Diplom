package ru.example.gbnotesapp.data.db

import ru.example.gbnotesapp.data.model.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    // Получить все заметки
    fun getAllNotes() = noteDao.getAllNotes()

    // Получить заметки по id папки
    fun getNotesByFolder(folderId: Int) = noteDao.getNotesByFolder(folderId)

    // Вставить новую заметку
    suspend fun insert(note: Note) = noteDao.insert(note)

    // Обновить существующую заметку
    suspend fun update(note: Note) = noteDao.update(note)

    // Удалить заметку
    suspend fun delete(note: Note) = noteDao.delete(note)
}
