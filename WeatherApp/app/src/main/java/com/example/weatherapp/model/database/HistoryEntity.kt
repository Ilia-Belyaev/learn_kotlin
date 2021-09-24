package com.example.weatherapp.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
    val condition: String,
    val timestamp: Long
)
