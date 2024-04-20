package ru.example.gbnotesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.model.Folder
import javax.inject.Inject

class ListFoldersViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel() {

    // LiveData для всех папок
    val allFolders = folderRepository.getAllFolders()

    // Функция для вставки новой папки
    fun insert(folder: Folder) = viewModelScope.launch {
        folderRepository.insert(folder)
    }

}
