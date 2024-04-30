package ru.example.gbnotesapp.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

        val note = arguments?.getParcelable<Note>("clickedNote")
        setSelectedFolder(note)
        setupButtonListeners()
        setEditTextTitleTextChangedListener()
        setEditTextContentTextChangedListener()

        if (note == null) {
            viewModel.createNewNote()
            setCurrentCreationDate()
            lifecycleScope.launch {
                val mainFolder = folderRepository.getMainFolder()
                viewModel.setSelectedFolder(mainFolder!!)
                binding.selectedFolder.text = mainFolder.name
            }
        } else {
            viewModel.setCurrentNote(note)
            binding.editTextTitle.setText(note.title)
            binding.editTextContent.setText(note.content)
            binding.textViewDate.text = note.creationDate
            lifecycleScope.launch {
                val currentFolder = folderRepository.getFolderById(note.folderId)
                currentFolder?.let { viewModel.setSelectedFolder(it) }
            }
        }

        lifecycleScope.launch {
            viewModel.allFolderNames.collect { folderNames ->
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.item_folder_to_note_fragment,
                    folderNames.toTypedArray()
                )
                (binding.containerListFolders.editText as? AutoCompleteTextView)?.setAdapter(
                    adapter
                )
            }
        }

        binding.listFoldersAutoCompleteTextView.setOnItemClickListener() { _, _, position, _ ->
            val selectedFolderName = viewModel.allFolderNames.value[position]
            binding.selectedFolder.text = selectedFolderName
            viewModel.onFolderSelected(position)
        }
    }

    private fun setSelectedFolder(note: Note?) {
        lifecycleScope.launch {
            val selectedFolderId = note?.folderId ?: 0
            if (selectedFolderId != 0) {
                val selectedFolder = selectedFolderId.let { folderRepository.getFolderById(it) }
                binding.selectedFolder.text = selectedFolder?.name
            } else
                binding.selectedFolder.text = "нет папки"
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
            val selectedFolder = viewModel.selectedFolder.value
            if (selectedFolder != null) {
                folderRepository.setSelectedFolder(selectedFolder)
            }
        }
        navigateBack()
    }

    private fun setEditTextTitleTextChangedListener() {
        binding.editTextTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
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
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
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

    private fun setCurrentCreationDate() {
        lifecycleScope.launch {
            val creationDate = viewModel.getCreationDate()
            binding.textViewDate.text = creationDate
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
