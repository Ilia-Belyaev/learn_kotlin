package com.example.weatherapp.model

import com.example.weatherapp.model.database.HistoryDAO
import com.example.weatherapp.model.database.HistoryEntity

class LocalRepositoryImpl(
    private val dao:HistoryDAO
):LocalRepository{
    override fun getAllHistory(): List<HistoryEntity> = dao.all()

    override fun saveEntity(weather: HistoryEntity) {
        dao.insert(weather)
    }


}