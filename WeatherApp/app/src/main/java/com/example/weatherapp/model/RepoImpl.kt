package com.example.weatherapp.model

class RepoImpl : Repo {
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()


    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()
}