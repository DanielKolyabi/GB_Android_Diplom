package ru.example.gbnotesapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.example.gbnotesapp.data.model.Folder

@Dao
interface FolderDao {

    @Query("SELECT * FROM folder")
    fun getAllFolders(): Flow<Folder>

    @Query("SELECT * FROM folder WHERE isSelected = 1")
    fun getSelectedFolder(): Flow<Folder?>

    @Insert
    suspend fun insert(folder: Folder)

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)
}
