package com.example.task.helperClass

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task.dao.RepositoryDAO
import com.example.task.entityTables.RepositoryEntity

@Database(entities = [RepositoryEntity::class], version = 1, exportSchema = false)
abstract class RoomDBHelper: RoomDatabase() {

    abstract fun getAllRepository(): RepositoryDAO

    companion object {
        @Volatile
        private var roomDBHelper: RoomDBHelper? = null

        fun getDatabase(context: Context): RoomDBHelper {
            return roomDBHelper ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDBHelper::class.java,
                    "repository_database"
                ).build()
                roomDBHelper = instance
                instance
            }
        }
    }
}