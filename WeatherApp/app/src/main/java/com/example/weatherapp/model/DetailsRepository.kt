package com.example.weatherapp.model

import retrofit2.Callback

interface DetailsRepository {
    fun getWeatherDetailFromServer(lat:Double,lon:Double,callback: Callback<WeatherDTO>)
}