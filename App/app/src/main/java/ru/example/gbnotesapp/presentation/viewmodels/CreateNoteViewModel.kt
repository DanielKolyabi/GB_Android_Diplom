package ru.example.gbnotesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.db.NoteRepository
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.data.model.Note
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CreateNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository
) : ViewModel() {
    private val _note = MutableStateFlow(Note(0, 0, "", "", ""))
    val note: StateFlow<Note> = _note

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote

    private val _isNoteModified = MutableStateFlow(false)
    val isNoteModified: StateFlow<Boolean> = _isNoteModified

    private val _allFolders = MutableStateFlow<List<Folder>>(listOf())
    val allFolders: StateFlow<List<Folder>> = _allFolders

    private val _allFolderNames = MutableStateFlow<List<String>>(emptyList())
    val allFolderNames: StateFlow<List<String>> = _allFolderNames

    private val _selectedFolder = MutableStateFlow<Folder?>(null)
    val selectedFolder: StateFlow<Folder?> = _selectedFolder

    private val _isNewNote = MutableStateFlow(true)
    val isNewNote: StateFlow<Boolean> = _isNewNote


    init {
        viewModelScope.launch {
            val folderList = folderRepository.getAllFolders()
            val folderNames = folderList.map { it.name }
            _allFolderNames.value = folderNames
            _allFolders.value = folderList
        }
    }

    fun onTitleChanged(newTitle: String) {
        _note.value = _note.value.copy(title = newTitle)
        _isNoteModified.value = true
    }

    fun onContentChanged(newContent: String) {
        _note.value = _note.value.copy(content = newContent)
        _isNoteModified.value = true
    }

    fun getCreationDate(): String {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm "))
        return currentTime + currentDate
    }

    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            val note = noteRepository.getNoteById(noteId)
            _currentNote.value = note
            _note.value = note ?: Note(0, 0, "", "", "")
        }
    }

    fun setCurrentNote(note: Note) {
        _currentNote.value = note
        _note.value = note ?: Note(0, 0, "", "", "")
        _isNewNote.value = false
    }

    fun setSelectedFolder(folder: Folder) {
        _selectedFolder.value = folder
    }

    fun createNewNote() {
        val creationDate = getCreationDate()
        _note.value = Note(0, 0, "", "", creationDate)
        _isNewNote.value = true
    }

    fun onSaveNote() {
        viewModelScope.launch {
            val currentNote = _note.value

            val selectedFolder = _selectedFolder.value
            if (selectedFolder != null) {
                val noteWithFolderId = currentNote.copy(folderId = selectedFolder.id!!)

                val noteWithCreationDate =
                    if (_currentNote.value != null && _isNoteModified.value) {
                        // Если текущая заметка существует и была изменена, обновляем дату создания
                        val creationDate = getCreationDate()
                        noteWithFolderId.copy(creationDate = creationDate)
                    } else {
                        noteWithFolderId
                    }

                if (_isNewNote.value) {
                    // Если создается новая заметка, вставляем ее
                    noteRepository.insert(noteWithCreationDate)
                    // Обновляем счетчик заметок в папке
                    folderRepository.updateNoteCount(noteWithCreationDate.folderId)
                } else {
                    // Если редактируется существующая заметка, обновляем ее
                    noteRepository.update(noteWithCreationDate)
                }
            }
        }
    }


    fun onFolderSelected(position: Int) {
        _selectedFolder.value = _allFolders.value[position]
        viewModelScope.launch {
            val selectedFolder = _selectedFolder.value
            if (selectedFolder != null) {
                folderRepository.setSelectedFolder(selectedFolder)
            }
        }
    }
}