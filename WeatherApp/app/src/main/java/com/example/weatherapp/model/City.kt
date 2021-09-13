package com.example.weatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class City(val city: String, val latitude: Double, val longitude: Double):Parcelable