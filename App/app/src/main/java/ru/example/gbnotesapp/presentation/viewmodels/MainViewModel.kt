package ru.example.gbnotesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.db.NoteRepository
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.data.model.Note
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {

    private val _allNotesByFolder = MutableStateFlow<List<Note>>(listOf())
    val allNotesByFolder = _allNotesByFolder

    private val _allFolders = MutableStateFlow<List<Folder>>(listOf())
    val allFolders = _allFolders

    // TODO новый функционал - под вопросом
    private val _selectedFolder = MutableStateFlow<Folder?>(null)
    val selectedFolder: StateFlow<Folder?> = _selectedFolder


    init {
        viewModelScope.launch {
            _allFolders.value = folderRepository.getAllFolders()
            createMainFolder()
        }
    }

    fun changeListNote(folderId: Int) {
        viewModelScope.launch {
            val currentFolder = folderRepository.getFolderById(folderId)
            if (currentFolder != null) {
                folderRepository.setSelectedFolder(currentFolder)
                val notesInFolder = if (currentFolder.id == 1)
                    noteRepository.getAllNotes()
                else
                    noteRepository.getNotesBySelectedFolder(folderId)

                _allNotesByFolder.value = notesInFolder
            }
        }
    }

//    fun setSelectedFolder(folder: Folder) {
//        _selectedFolder.value = folder
//    }


    // Функция для вставки новой заметки
    fun insertNote(note: Note) = viewModelScope.launch {
        noteRepository.insert(note)
    }

    // Функция для вставки новой папки
    fun insertFolder(folder: Folder) = viewModelScope.launch {
        folderRepository.insert(folder)
        updateFolders()
    }

    private fun createMainFolder() {
        viewModelScope.launch {
            val mainFolderName = "Все"
            val firstFolder = folderRepository.getFolderById(1)
            if (firstFolder == null) {
                val mainFolder = Folder(id = null, name = mainFolderName, isSelected = true)
                insertFolder(mainFolder)
            }
        }
    }

    // Функция для обновления
    fun updateFolders() {
        viewModelScope.launch {
            allFolders.value = folderRepository.getAllFolders()
        }
    }
}