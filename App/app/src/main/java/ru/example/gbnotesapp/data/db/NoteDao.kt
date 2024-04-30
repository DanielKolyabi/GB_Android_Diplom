package ru.example.gbnotesapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ru.example.gbnotesapp.data.model.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY creationDate DESC")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE folderId = :folderId ORDER BY creationDate DESC")
    suspend fun getNotesByFolder(folderId: Int): List<Note>

    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int): Note

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note WHERE folderId = :folderId")
    suspend fun deleteNotesInFolder(folderId: Int)
}
