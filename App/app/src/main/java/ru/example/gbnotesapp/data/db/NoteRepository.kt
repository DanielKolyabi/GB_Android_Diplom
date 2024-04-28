package ru.example.gbnotesapp.data.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.data.model.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val folderRepository: FolderRepository
) {

    // Получить все заметки
    suspend fun getAllNotes() = noteDao.getAllNotes()

    // Получить заметку по id
    suspend fun getNoteById(noteId: Int) = noteDao.getNoteById(noteId)

    // Вставить новую заметку
    suspend fun insert(note: Note) {
        noteDao.insert(note)
        folderRepository.updateNoteCount(note.folderId)
    }

    // Обновить существующую заметку
    suspend fun update(note: Note) = noteDao.update(note)

    // Удалить заметку
    suspend fun delete(note: Note) {
        noteDao.delete(note)
        folderRepository.updateNoteCount(note.folderId)
    }

    // Получить заметки из выбранной папки
    suspend fun getNotesBySelectedFolder(folderId: Int): List<Note> {
        return noteDao.getNotesByFolder(folderId)
    }


}
