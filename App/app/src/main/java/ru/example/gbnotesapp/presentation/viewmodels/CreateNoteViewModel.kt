package ru.example.gbnotesapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.data.db.NoteRepository
import ru.example.gbnotesapp.data.model.Note
import javax.inject.Inject

class CreateNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _note = MutableStateFlow(Note(0,0, "", "", 0))
    val note: StateFlow<Note> = _note

    fun onTitleChanged(newTitle: String) {
        _note.value = _note.value.copy(title = newTitle)
    }

    fun onContentChanged(newContent: String) {
        _note.value = _note.value.copy(content = newContent)
    }

    fun onSaveNote() {
        viewModelScope.launch {
            noteRepository.insert(_note.value)
        }
    }
}