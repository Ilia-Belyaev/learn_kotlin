package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.LocalRepositoryImpl
import com.example.weatherapp.model.database.HistoryEntity
import com.example.weatherapp.view.App

class HistoryViewModel : ViewModel(){
    private val historyRepository = LocalRepositoryImpl(App.getHistoryDao())

    fun getAllHistory():List<HistoryEntity> = historyRepository.getAllHistory()}