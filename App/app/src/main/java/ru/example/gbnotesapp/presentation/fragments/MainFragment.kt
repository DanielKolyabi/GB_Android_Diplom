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
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.databinding.FragmentMainBinding
import ru.example.gbnotesapp.presentation.ViewModelFactory
import ru.example.gbnotesapp.presentation.viewmodels.FolderAdapter
import ru.example.gbnotesapp.presentation.viewmodels.MainViewModel
import ru.example.gbnotesapp.presentation.viewmodels.NoteAdapter
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var mainViewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NoteAdapter()
        binding.recyclerViewNotes.adapter = adapter

        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerViewNotes.layoutManager = layoutManager

        val folderAdapter = FolderAdapter()
        binding.recyclerViewFolders.adapter = folderAdapter

        val layoutManagerFolders = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFolders.layoutManager = layoutManagerFolders


        // Обновляем список заметок при изменении данных
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allNotes.collect { notes ->
                adapter.submitList(notes)
            }
        }
        // Обновляем список папок при изменении данных
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allFolders.collect { folders ->
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
        fun newInstance() = MainFragment()
    }
}