package ru.example.gbnotesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.example.gbnotesapp.presentation.viewmodels.CreateNoteViewModel
import ru.example.gbnotesapp.presentation.viewmodels.ListFoldersViewModel
import ru.example.gbnotesapp.presentation.viewmodels.MainViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val mainViewModel: MainViewModel,
    private val createNoteViewModel: CreateNoteViewModel,
    private val listFoldersViewModel: ListFoldersViewModel,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return mainViewModel as T
        }
        if (modelClass.isAssignableFrom(CreateNoteViewModel::class.java)) {
            return createNoteViewModel as T
        }
        if (modelClass.isAssignableFrom(ListFoldersViewModel::class.java)) {
            return listFoldersViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}