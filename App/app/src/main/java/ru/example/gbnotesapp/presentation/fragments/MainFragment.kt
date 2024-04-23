package ru.example.gbnotesapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.db.NoteRepository
import ru.example.gbnotesapp.data.model.Note
import ru.example.gbnotesapp.databinding.FragmentMainBinding
import ru.example.gbnotesapp.presentation.ViewModelFactory
import ru.example.gbnotesapp.presentation.viewmodels.FolderAdapter
import ru.example.gbnotesapp.presentation.viewmodels.ListFoldersViewModel
import ru.example.gbnotesapp.presentation.viewmodels.MainViewModel
import ru.example.gbnotesapp.presentation.viewmodels.NoteAdapter
import ru.example.gbnotesapp.presentation.viewmodels.OnNoteClickListener
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(), OnNoteClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { mainViewModelFactory }

    @Inject
    lateinit var listFoldersViewModelFactory: ViewModelFactory
    private val listFoldersViewModel: ListFoldersViewModel by viewModels { listFoldersViewModelFactory }

    @Inject
    lateinit var folderRepository: FolderRepository

    @Inject
    lateinit var noteRepository: NoteRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = NoteAdapter(this)
        binding.recyclerViewNotes.adapter = adapter

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewNotes.layoutManager = layoutManager

        val folderAdapter = FolderAdapter(mainViewModel, listFoldersViewModel, folderRepository, noteRepository, VIEW_TYPE_MAIN,adapter)
        binding.recyclerViewFolders.adapter = folderAdapter

        val layoutManagerFolders = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFolders.layoutManager = layoutManagerFolders

        // Обновляем список заметок при изменении данных
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.allNotes.collect { notes ->
                adapter.submitList(notes)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            folderRepository.getSelectedFolder().collect { selectedFolder ->
                val notesInSelectedFolder = noteRepository.getNotesBySelectedFolder().first()
                adapter.submitList(notesInSelectedFolder)
            }
        }

        // Обновляем список папок при изменении данных
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.allFolders.collect { folders ->
                folderAdapter.submitList(folders)
            }
        }

        binding.buttonCreateNewNote.setOnClickListener{
            findNavController().navigate(R.id.action_MainFragment_to_CreateNoteFragment)
        }

        binding.buttonNavigationToFolders.setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_ListFoldersFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val VIEW_TYPE_MAIN = 0
        private const val VIEW_TYPE_LIST = 1
    }

    override fun onNoteClick(note: Note) {
        val action = MainFragmentDirections.actionMainFragmentToCreateNoteFragment(note.folderId)
        findNavController().navigate(action)
    }
}

/**
 * Отображение названия папки в TextView при переходе из MainFragment в CreateNoteFragment:
 * Тебе нужно передать выбранную папку как аргумент при навигации из MainFragment в CreateNoteFragment.
 * Затем ты можешь получить эту папку в CreateNoteFragment и установить ее
 * название в TextView. Вот как это можно сделать:
 *
 * val action = MainFragmentDirections.actionMainFragmentToCreateNoteFragment(note, selectedFolder)
 * findNavController().navigate(action)
 *
 */