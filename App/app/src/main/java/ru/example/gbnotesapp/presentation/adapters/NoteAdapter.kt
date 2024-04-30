package ru.example.gbnotesapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.example.gbnotesapp.data.model.Note
import ru.example.gbnotesapp.databinding.ItemNoteToMainFragmentBinding

class NoteAdapter(
    private val clickNote: (note: Note) -> Unit,
    private val longClickNote: (note: Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(
    DiffUtilCallback()
) {

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

    inner class NoteViewHolder(
        private val binding: ItemNoteToMainFragmentBinding,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.apply {
                noteTitle.text = note.title
                noteContent.text = note.content
                noteDate.text = note.creationDate.toString()
                root.setOnClickListener {
                    clickNote(note)
                }
                root.setOnLongClickListener {
                    longClickNote(note)
                    true
                }
            }
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) =
        oldItem == newItem
}

