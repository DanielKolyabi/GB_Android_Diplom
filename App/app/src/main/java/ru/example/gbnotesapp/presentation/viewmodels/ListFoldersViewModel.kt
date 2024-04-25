package ru.example.gbnotesapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.model.Folder
import javax.inject.Inject

class ListFoldersViewModel @Inject constructor(
    private val folderRepository: FolderRepository
) : ViewModel() {

    // LiveData для всех папок
//    var allFolders = folderRepository.getAllFolders()
    private val _allFolders = MutableStateFlow<List<Folder>>(listOf())
    val allFolders = _allFolders

    init {
        viewModelScope.launch {
            _allFolders.value = folderRepository.getAllFolders()
//            createMainFolder()
        }
    }

    // Функция для вставки новой папки
    fun insert(folder: Folder) = viewModelScope.launch {
        folderRepository.insert(folder)
//        allFolders.value = folderRepository.getAllFolders()
        updateFolders()
    }

    // Функция для обнавления
    private fun updateFolders() {
        viewModelScope.launch {
            allFolders.value = folderRepository.getAllFolders()
        }
    }

//    private fun createMainFolder() {
//        viewModelScope.launch {
//            val mainFolderName = "Все"
//            val firstFolder = folderRepository.getFolderById(1)
//            if (firstFolder == null) {
//                val mainFolder = Folder(id = null, name = mainFolderName, isSelected = true)
//                insert(mainFolder)
//            }
//        }
//    }
//    private suspend fun doesFolderExist(folderName: String): Boolean {
//        val folders = folderRepository.getAllFolders()
//        val firstFolder = folders.firstOrNull()
//        return firstFolder?.name == folderName
//    }
}
