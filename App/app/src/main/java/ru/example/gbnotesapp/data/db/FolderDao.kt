package ru.example.gbnotesapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.example.gbnotesapp.data.model.Folder
import ru.example.gbnotesapp.data.model.Note

@Dao
interface FolderDao {

    @Query("SELECT * FROM folder")
    suspend fun getAllFolders(): List<Folder>

//    fun getAllFolders(): Flow<List<Folder>>
    


    @Query("SELECT * FROM folder WHERE isSelected = 1")
    suspend fun getSelectedFolder(): Folder?

    @Query("SELECT * FROM folder WHERE id = :folderId")
    suspend fun getFolderById(folderId: Int): Folder?

    @Insert
    suspend fun insert(folder: Folder)

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)
}
