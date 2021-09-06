package com.example.weatherapp.model

class Weather(val city:City = getDefaultCity(),val temperature: Int = 0, val feelsLike: Int = 0 ) {
}
fun getDefaultCity()=City("Ковров", 56.21, 41.3192)