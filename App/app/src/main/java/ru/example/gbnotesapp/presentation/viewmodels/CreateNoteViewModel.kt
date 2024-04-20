package ru.example.gbnotesapp.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.data.db.NoteRepository
import ru.example.gbnotesapp.data.model.Note
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import java.time.LocalDate

class CreateNoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {
    private val _note = MutableStateFlow(Note(0,0, "", "", ""))
    val note: StateFlow<Note> = _note

    fun onTitleChanged(newTitle: String) {
        _note.value = _note.value.copy(title = newTitle)
    }

    fun onContentChanged(newContent: String) {
        _note.value = _note.value.copy(content = newContent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSaveNote() {
        viewModelScope.launch {
            val currentNote = _note.value
            val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            val noteWithCurrentDate = currentNote.copy(creationDate = currentDate)
            noteRepository.insert(noteWithCurrentDate)
        }
    }
}