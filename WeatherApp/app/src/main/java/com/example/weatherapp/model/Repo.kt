package com.example.weatherapp.model

interface Repo {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}