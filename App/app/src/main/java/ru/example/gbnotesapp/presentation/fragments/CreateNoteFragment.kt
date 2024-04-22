package ru.example.gbnotesapp.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.data.model.Note
import ru.example.gbnotesapp.databinding.FragmentCreateNoteBinding
import ru.example.gbnotesapp.presentation.ViewModelFactory
import ru.example.gbnotesapp.presentation.viewmodels.CreateNoteViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CreateNoteFragment : Fragment() {

    private var _binding: FragmentCreateNoteBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var createNoteViewModelFactory: ViewModelFactory
    private val viewModel: CreateNoteViewModel by viewModels { createNoteViewModelFactory }

    private lateinit var folderAdapter: ArrayAdapter<Folder>

    @Inject
    lateinit var folderRepository: FolderRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonListeners()
        setEditTextTitleTextChangedListener()
        setEditTextContentTextChangedListener()

        val note = arguments?.getParcelable<Note>("note")
        viewModel.setCurrentNote(note)

        val selectedFolder = arguments?.getParcelable<Folder>("selectedFolder")
        binding.selectedFolder.text = selectedFolder?.name

        if (note == null) {
            viewModel.createNewNote()
            setCurrentCreationDate()
        } else {
            binding.editTextTitle.setText(note.title)
            binding.editTextContent.setText(note.content)
            binding.textViewDate.text = note.creationDate
        }


        folderAdapter= ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item)
        binding.spinnerFolders.adapter = folderAdapter

        loadFolders()

        binding.spinnerFolders.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedFolderName = parent.getItemAtPosition(position) as String
                lifecycleScope.launch {
                    viewModel.allFolders.collect { folders ->
                        val selectedFolder = folders.find { it.name == selectedFolderName }
                        if (selectedFolder != null) {
                            viewModel.onFolderSelected(selectedFolder)
                            binding.selectedFolder.text = selectedFolderName
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Ничего не делать
            }
        }


    }

    private fun setupButtonListeners() {
        binding.buttonBack.setOnClickListener { navigateBack() }
        binding.buttonDone.setOnClickListener { saveNoteAndNavigateBack() }
    }

    private fun navigateBack() {
        findNavController().navigate(R.id.action_createNoteFragment_to_MainFragment)
    }

    private fun saveNoteAndNavigateBack() {
        lifecycleScope.launch {
            viewModel.onSaveNote()
        }
        lifecycleScope.launch {
            val selectedFolder = binding.spinnerFolders.selectedItem as Folder
            folderRepository.setSelectedFolder(selectedFolder)
        }
        navigateBack()
    }

    private fun setCurrentCreationDate() {
        lifecycleScope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val creationDate = viewModel.getCreationDate()
                binding.textViewDate.text = creationDate
            }
        }
    }

    private fun setEditTextTitleTextChangedListener() {
        binding.editTextTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Ничего не делать
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onTitleChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Ничего не делать
            }
        })
    }

    private fun setEditTextContentTextChangedListener() {
        binding.editTextContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Ничего не делать
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                viewModel.onContentChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
                // Ничего не делать
            }
        })
    }

    private fun loadFolders() {
        lifecycleScope.launch {
            viewModel.allFolders.collect { folders ->
                folderAdapter.clear()
//                val folderNames = folders.map { it.name }
//                folderAdapter.addAll(folderNames)
                folderAdapter.addAll(folders)
                folderAdapter.notifyDataSetChanged()
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object
}
