package com.example.weatherapp.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.view.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(
    private val lat: Double,
    private val lon: Double,
    private val listener: WeatherLoaderListener
) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun goToInternet() {

        Thread {

        }.start()
    }

    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }

}