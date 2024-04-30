package ru.example.gbnotesapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.databinding.ItemFolderToMainFragmentBinding

class FolderAdapter(
    private val onChangeShowNote: (folderId: Int) -> Unit,
) : ListAdapter<Folder, FolderAdapter.MainFolderViewHolder>(FolderDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFolderViewHolder {
        return MainFolderViewHolder(
            ItemFolderToMainFragmentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainFolderViewHolder, position: Int) {
        val currentFolder = getItem(position)
        holder.bind(currentFolder)
    }

    inner class MainFolderViewHolder(private val binding: ItemFolderToMainFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(folder: Folder) {
            binding.buttonFolderInMainRecyclerView.text = folder.name
            binding.buttonFolderInMainRecyclerView.setOnClickListener {
                folder.id?.let { it1 -> onChangeShowNote(it1) }
            }
        }
    }
}

class FolderDiffUtilCallback : DiffUtil.ItemCallback<Folder>() {
    override fun areItemsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem == newItem
}
