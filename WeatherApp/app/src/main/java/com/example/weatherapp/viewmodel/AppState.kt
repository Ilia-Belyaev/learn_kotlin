package com.example.weatherapp.viewmodel

import com.example.weatherapp.model.Weather


sealed class AppState {
    data class Success(val weather: List<Weather>): AppState()
    data class Error(val error:Throwable): AppState()
    object Loading: AppState()
}