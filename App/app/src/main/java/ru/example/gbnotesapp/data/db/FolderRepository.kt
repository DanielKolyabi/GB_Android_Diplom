package ru.example.gbnotesapp.data.db

import ru.example.gbnotesapp.data.model.Folder
import javax.inject.Inject

class FolderRepository @Inject constructor(private val folderDao: FolderDao) {

    // Получить все папки
    fun getAllFolders() = folderDao.getAllFolders()

    // Вставить новую папку
    suspend fun insert(folder: Folder) = folderDao.insert(folder)

    // Обновить существующую папку
    suspend fun update(folder: Folder) = folderDao.update(folder)

    // Удалить папку
    suspend fun delete(folder: Folder) = folderDao.delete(folder)

    // Получить выбранную папку
    fun getSelectedFolder() = folderDao.getSelectedFolder()
}
