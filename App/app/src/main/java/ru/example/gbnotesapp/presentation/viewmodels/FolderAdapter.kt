package ru.example.gbnotesapp.presentation.viewmodels
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.databinding.ItemFolderToMainFragmentBinding

class FolderAdapter : ListAdapter<Folder, FolderViewHolder>(FolderDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderToMainFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val currentFolder = getItem(position)
        holder.bind(currentFolder)
    }
}

class FolderDiffUtilCallback : DiffUtil.ItemCallback<Folder>() {
    override fun areItemsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder) =
        oldItem == newItem
}

class FolderViewHolder(private val binding: ItemFolderToMainFragmentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(folder: Folder) {
        binding.apply {
            buttonFolderInMainRecyclerView.text = folder.name
            buttonFolderInMainRecyclerView
        }
    }
}
