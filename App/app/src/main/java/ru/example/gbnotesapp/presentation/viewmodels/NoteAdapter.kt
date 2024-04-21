package ru.example.gbnotesapp.presentation.viewmodels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.example.gbnotesapp.R
import ru.example.gbnotesapp.data.model.Note
import ru.example.gbnotesapp.databinding.ItemNoteToMainFragmentBinding

class NoteAdapter : ListAdapter<Note, NoteViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteToMainFragmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.bind(currentNote)
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem == newItem
}


class NoteViewHolder(private val binding: ItemNoteToMainFragmentBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(note: Note) {
        binding.apply {
            noteTitle.text = note.title
            noteContent.text = note.content
            noteDate.text = note.creationDate.toString()
            root.setOnClickListener {
                val bundle = Bundle().apply {
                    putParcelable("note", note)
                }
                it.findNavController().navigate(R.id.action_MainFragment_to_CreateNoteFragment, bundle)
            }
        }
    }
}
