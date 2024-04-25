package ru.example.gbnotesapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
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
import ru.example.gbnotesapp.presentation.viewmodels.ListFoldersViewModel
import ru.example.gbnotesapp.presentation.viewmodels.MainViewModel

class FolderAdapter(
//    private val mainViewModel: MainViewModel,
//    private val folderRepository: FolderRepository,
//    private val noteRepository: NoteRepository,
//    private val noteAdapter: NoteAdapter,
    private val onChangeShowNote: (Int) -> Unit,
) : ListAdapter<Folder, MainFolderViewHolder>(FolderDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFolderViewHolder {
//        val binding = ItemFolderToMainFragmentBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
        return MainFolderViewHolder(
            ItemFolderToMainFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
//            binding,
////            mainViewModel,
//            noteAdapter
//            folderRepository,
//            noteRepository,
//        )
    }

    override fun onBindViewHolder(holder: MainFolderViewHolder, position: Int) {
        val currentFolder = getItem(position)

        holder.binding.buttonFolderInMainRecyclerView.text = currentFolder.name
        holder.binding.buttonFolderInMainRecyclerView.setOnClickListener {
            onChangeShowNote(currentFolder.id!!)
        }

//        if (holder is MainFolderViewHolder) {
//            holder.bind(currentFolder)
//        } else if (holder is ListFolderViewHolder) {
//            holder.bind(currentFolder)
//        }
    }
}

class MainFolderViewHolder(val binding: ItemFolderToMainFragmentBinding): RecyclerView.ViewHolder(binding.root)
//    private val binding: ItemFolderToMainFragmentBinding,
////    private val viewModel: MainViewModel,
////    private val folderRepository: FolderRepository,
////    private val noteRepository: NoteRepository,
//    private val noteAdapter: NoteAdapter
//) : RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(currentFolder: Folder) {
//        binding.buttonFolderInMainRecyclerView.text = currentFolder.name
//        binding.buttonFolderInMainRecyclerView.setOnClickListener {
//
////            viewModel.viewModelScope.launch {
////                folderRepository.setSelectedFolder(folder)
////                val notesInSelectedFolder = noteRepository.getNotesBySelectedFolder().first()
////                noteAdapter.submitList(notesInSelectedFolder)
////            }
//        }
//    }
//}

class FolderDiffUtilCallback : DiffUtil.ItemCallback<Folder>() {
    override fun areItemsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem == newItem
}
