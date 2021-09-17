package com.example.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class WeatherDTO(
    val fact: FactDTO?

) {
    @Parcelize
    class FactDTO (
        val temp: Int?,
        val feels_like:Int?,
        val condition:String?
    ):Parcelable
}