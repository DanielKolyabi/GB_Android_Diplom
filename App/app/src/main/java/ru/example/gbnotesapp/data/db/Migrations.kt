package ru.example.gbnotesapp.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Создаем новую таблицу с nullable id
        db.execSQL(
            "CREATE TABLE Folder_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "noteCount INTEGER NOT NULL DEFAULT 0," +
                    "isSelected INTEGER NOT NULL DEFAULT 0)"
        )

        // Копируем данные из старой таблицы в новую
        db.execSQL(
            "INSERT INTO Folder_new (id, name, noteCount, isSelected) " +
                    "SELECT id, name, noteCount, isSelected FROM Folder"
        )

        // Удаляем старую таблицу
        db.execSQL("DROP TABLE Folder")

        // Переименовываем новую таблицу, чтобы она заменила старую
        db.execSQL("ALTER TABLE Folder_new RENAME TO Folder")
    }
}