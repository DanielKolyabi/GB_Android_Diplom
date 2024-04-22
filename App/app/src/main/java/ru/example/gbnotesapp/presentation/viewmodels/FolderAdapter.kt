package ru.example.gbnotesapp.presentation.viewmodels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.example.gbnotesapp.data.db.FolderRepository
import ru.example.gbnotesapp.data.db.NoteRepository
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.databinding.ItemFolderToListBinding
import ru.example.gbnotesapp.databinding.ItemFolderToMainFragmentBinding

class FolderAdapter(
    private val mainViewModel: MainViewModel,
    private val listFoldersViewModel: ListFoldersViewModel,
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository,
    private val viewType: Int,
    private val noteAdapter: NoteAdapter
) : ListAdapter<Folder, RecyclerView.ViewHolder>(FolderDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MAIN) {
            val binding = ItemFolderToMainFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            MainFolderViewHolder(binding, mainViewModel, folderRepository, noteRepository, noteAdapter)
        } else {
            val binding = ItemFolderToListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ListFolderViewHolder(binding, listFoldersViewModel, folderRepository)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentFolder = getItem(position)
        if (holder is MainFolderViewHolder) {
            holder.bind(currentFolder)
        } else if (holder is ListFolderViewHolder) {
            holder.bind(currentFolder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    companion object {
        private const val VIEW_TYPE_MAIN = 0
        private const val VIEW_TYPE_LIST = 1
    }
}

class FolderDiffUtilCallback : DiffUtil.ItemCallback<Folder>() {
    override fun areItemsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem == newItem
}

class MainFolderViewHolder(
    private val binding: ItemFolderToMainFragmentBinding,
    private val viewModel: MainViewModel,
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository,
    private val noteAdapter: NoteAdapter
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.buttonFolderInMainRecyclerView.setOnClickListener {
            val folder = binding.folder
            if (folder != null) {
                viewModel.viewModelScope.launch {
                    folderRepository.setSelectedFolder(folder)
                    val notesInSelectedFolder = noteRepository.getNotesBySelectedFolder().first()
                    noteAdapter.submitList(notesInSelectedFolder)
                }
            }
        }
    }

    fun bind(folder: Folder) {
        binding.folder = folder
    }



//    fun bind(folder: Folder) {
//        binding.apply {
//            buttonFolderInMainRecyclerView.text = folder.name
//            buttonFolderInMainRecyclerView.setOnClickListener {
//                viewModel.viewModelScope.launch {
//                    folderRepository.setSelectedFolder(folder)
//                    val notesInSelectedFolder = noteRepository.getNotesBySelectedFolder().first()
//                    noteAdapter.submitList(notesInSelectedFolder)
//                }
//            }
//        }
//    }
}

class ListFolderViewHolder(
    private val binding: ItemFolderToListBinding,
    private val viewModel: ListFoldersViewModel,
    private val folderRepository: FolderRepository
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(folder: Folder) {
        binding.apply {
            folderName.text = folder.name
            noteCount.text = folder.noteCount.toString()
        }
    }
}
