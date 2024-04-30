package ru.example.gbnotesapp

import android.app.Application
import androidx.room.Room
import dagger.hilt.android.HiltAndroidApp
import ru.example.gbnotesapp.data.db.AppDatabase
import ru.example.gbnotesapp.data.db.MIGRATION_1_2

@HiltAndroidApp
class App : Application() {
    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
}