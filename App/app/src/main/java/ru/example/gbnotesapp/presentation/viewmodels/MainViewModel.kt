package ru.example.gbnotesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // LiveData для всех заметок
    val allNotes = noteRepository.getAllNotes()

    // LiveData для всех папок
    val allFolders = folderRepository.getAllFolders()

    // Функция для вставки новой заметки
    fun insert(note: Note) = viewModelScope.launch {
        noteRepository.insert(note)
    }

    // Функция для вставки новой папки
    fun insert(folder: Folder) = viewModelScope.launch {
        folderRepository.insert(folder)
    }


}