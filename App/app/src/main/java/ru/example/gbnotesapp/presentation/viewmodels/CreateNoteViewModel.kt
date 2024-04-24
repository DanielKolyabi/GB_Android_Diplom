package ru.example.gbnotesapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

//    val allFolders = folderRepository.getAllFolders()

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

    fun setCurrentNote(note: Note?) {
        _currentNote.value = note
        _note.value = note ?: Note(0, 0, "", "", "")
    }

    fun createNewNote() {
        val creationDate = getCreationDate()
        _note.value = Note(0, 0, "", "", creationDate)
    }

    fun onSaveNote() {
        viewModelScope.launch {
            val currentNote = _note.value
            val noteWithCreationDate = if (_currentNote.value != null && _isNoteModified.value) {
                // Если текущая заметка существует и была изменена, обновляем дату создания
                val creationDate = getCreationDate()
                currentNote.copy(creationDate = creationDate)
            } else {
                currentNote
            }

            if (_currentNote.value != null) {
                // Если текущая заметка существует, обновляем ее
                noteRepository.update(noteWithCreationDate)
            } else {
                // Если текущая заметка не существует, создаем новую
                noteRepository.insert(noteWithCreationDate)
                // Обновляем счетчик заметок в папке
                folderRepository.updateNoteCount(noteWithCreationDate.folderId)
            }
        }
    }

    fun onFolderSelected(folder: Folder) {
        if (folder.id != null) {
            _note.value = _note.value.copy(folderId = folder.id)
        }
    }
}