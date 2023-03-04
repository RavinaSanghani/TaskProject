package com.example.task.entityTables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositoryTable")
data class RepositoryEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "htmlUrl") val htmlUrl: String
){
    @PrimaryKey(autoGenerate = true) var id = 0
}
