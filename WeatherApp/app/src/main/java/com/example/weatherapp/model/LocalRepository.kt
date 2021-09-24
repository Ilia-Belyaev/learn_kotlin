package com.example.weatherapp.model

import com.example.weatherapp.model.database.HistoryEntity

interface LocalRepository {
    fun getAllHistory():List<HistoryEntity>
    fun saveEntity(weather: HistoryEntity)
}