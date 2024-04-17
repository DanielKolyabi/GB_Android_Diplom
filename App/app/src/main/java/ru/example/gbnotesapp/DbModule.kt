package ru.example.gbnotesapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.example.gbnotesapp.data.db.AppDatabase
import ru.example.gbnotesapp.data.db.FolderDao
import ru.example.gbnotesapp.data.db.NoteDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "note_database"
    ).build()

    @Provides
    @Singleton
    fun provideDao(db: AppDatabase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideFolderDao(db: AppDatabase): FolderDao = db.folderDao()

}

