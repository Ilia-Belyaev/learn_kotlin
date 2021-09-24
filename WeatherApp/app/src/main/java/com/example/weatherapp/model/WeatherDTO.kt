package com.example.weatherapp.model

data class WeatherDTO(
    val fact: FactDTO?

) {
    class FactDTO (
        val temp: Int?,
        val feels_like:Int?,
        val condition:String?
    )
}