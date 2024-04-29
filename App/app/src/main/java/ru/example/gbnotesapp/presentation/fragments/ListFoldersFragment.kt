package ru.example.gbnotesapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.db.NoteRepository
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.databinding.FragmentListFoldersBinding
import ru.example.gbnotesapp.presentation.ViewModelFactory
import ru.example.gbnotesapp.presentation.adapters.ListFolderAdapter
import ru.example.gbnotesapp.presentation.viewmodels.ListFoldersViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ListFoldersFragment : Fragment() {

    private var _binding: FragmentListFoldersBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var listFoldersViewModelFactory: ViewModelFactory
    private val listFoldersViewModel: ListFoldersViewModel by viewModels { listFoldersViewModelFactory }

    // TODO попытка 2
    private val listFolderAdapter: ListFolderAdapter = ListFolderAdapter { folder ->
        val sharedPreferences = requireActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("selectedFolderId", folder.id!!)
        editor.apply()

        findNavController().navigate(R.id.action_ListFoldersFragment_to_MainFragment)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListFoldersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFolders.adapter = listFolderAdapter

        // Обновляем список папок при изменении данных
        viewLifecycleOwner.lifecycleScope.launch {
            listFoldersViewModel.allFolders.collect { folders ->
                listFolderAdapter.submitList(folders)
            }
        }
        clickButtonBack()
        clickButtonNewFolder()
    }

    private fun clickButtonBack() {
        binding.buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_ListFoldersFragment_to_MainFragment)
        }
    }

    private fun clickButtonNewFolder() {
        binding.newFolderButton.setOnClickListener {
            showNewFolderDialog()
        }
    }

    private fun showNewFolderDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Новая папка")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.filters =
            arrayOf<InputFilter>(InputFilter.LengthFilter(15)) // Ограничение на ввод названия папки 15 символами
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val folderName = input.text.toString()
            if (folderName.isNotEmpty() && !doesFolderExist(folderName)) {
                createNewFolder(folderName)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Папка с таким названием уже существует",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Отмена") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun doesFolderExist(folderName: String): Boolean {
        val allListFolders = listFoldersViewModel.allFolders.value
        return allListFolders.any { it.name == folderName }
    }

    private fun createNewFolder(folderName: String) {
        if (folderName.isBlank()) {
            Toast.makeText(requireContext(), "Имя папки не может быть пустым", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val newFolder = Folder(null, folderName, 0, false)
        listFoldersViewModel.insert(newFolder)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
