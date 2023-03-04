package com.example.task.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.task.entityTables.RepositoryEntity

@Dao
interface  RepositoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: RepositoryEntity)

    @Query("Select * from repositoryTable order by id ASC")
    fun getAllRepository(): LiveData<List<RepositoryEntity>>

}