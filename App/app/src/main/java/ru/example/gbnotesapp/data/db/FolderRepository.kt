package ru.example.gbnotesapp.data.db

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import ru.example.gbnotesapp.data.model.Folder
import javax.inject.Inject

class FolderRepository @Inject constructor(
    private val folderDao: FolderDao,
    private val noteDao: NoteDao
) {

    // Получить все папки
    fun getAllFolders() = folderDao.getAllFolders()

    // Вставить новую папку
    suspend fun insert(folder: Folder) = folderDao.insert(folder)

    // Обновить существующую папку
    suspend fun update(folder: Folder) = folderDao.update(folder)

    suspend fun updateNoteCount(folderId: Int) {
        val noteCount = noteDao.getNotesByFolder(folderId).count()
        val folder = folderDao.getFolderById(folderId)
        if (folder != null) {
            val updatedFolder = folder.copy(noteCount = noteCount)
            folderDao.update(updatedFolder)
        }
    }

    // Удалить папку
    suspend fun delete(folder: Folder) {
        if (folder.name != "Все") {
            folderDao.delete(folder)
        }
    }

    suspend fun setSelectedFolder(folder: Folder) {
        val selectedFolder = getSelectedFolder().first()
        if (selectedFolder != null) {
            // Сбросить флаг выбранной папки для текущей выбранной папки
            update(selectedFolder.copy(isSelected = false))
        }
        // Установить флаг выбранной папки для новой выбранной папки
        update(folder.copy(isSelected = true))
    }





    // Получить выбранную папку
    fun getSelectedFolder() = folderDao.getSelectedFolder()
}
