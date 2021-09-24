package com.example.weatherapp.model

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherapp.view.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

const val MAIN_SERVICE_EXTRA = "MainServiceExtra"
const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"
const val DETAILS_INTENT_FILTER = " DETAILS INTENT FILTER"
const val FACT_WEATHER_EXTRA = " FACT WEATHER EXTRA"
const val RESULT_EXTRA = "RESULT EXTRA"
const val SUCCESS_RESULT="SUCCESS RESULT"
const val ERROR_EMPTY_DATA_RESULT = "ERROR EMPTY DATA RESULT"

class MainService(name: String = "MainService") : IntentService(name) {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {

        Log.d("DEBUG", "Service started ${Thread.currentThread()}")

        intent?.let {
            val lat = intent.getDoubleExtra(LATITUDE_EXTRA, 0.0)
            val lon = intent.getDoubleExtra(LONGITUDE_EXTRA, 0.0)

            if (lat == 0.0 && lon == 0.0) {
                onEmptyData()
            } else {
                loadWeather(lat, lon)
            }
        } ?: onEmptyIntent()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather(lat: Double, lon: Double) {
        var urlConnection: HttpsURLConnection? = null

        val uri = try {
            URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&lang=ru_RU")
        } catch (e: MalformedURLException) {
            onMalformedURL()
            return
        }

        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.apply {
                requestMethod = "GET"
                readTimeout = 10000
                addRequestProperty("X-Yandex-API-Key", "46fe3a8c-5ac7-4650-8578-40ab8b2a1082")
            }

            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))

            val weatherDTO: WeatherDTO = Gson().fromJson(result, WeatherDTO::class.java)


            Log.d("DEBUG", "onSuccessResponse ${Thread.currentThread()}")
            onResponse(weatherDTO)
        } catch (e: Exception) {
            onErrorResponse(e.message ?: "Unknown Error")
            Log.e("", "FAILED", e)
        } finally {
            urlConnection?.disconnect()
        }
    }

    private fun onResponse(weatherDTO: WeatherDTO) {
        weatherDTO.fact?.let {
            onSuccessResponse(it)
        } ?: onEmptyResponse()
    }

    private fun onSuccessResponse(weatherFact: WeatherDTO.FactDTO) {
        Log.d("DEBUG", "Send broadcast ${Thread.currentThread()}")
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, SUCCESS_RESULT)
                    .putExtra(FACT_WEATHER_EXTRA, weatherFact)
            )
    }

    private fun onMalformedURL() {
        TODO("Not yet implemented")
    }

    private fun onErrorResponse(s: String) {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, ERROR_EMPTY_DATA_RESULT)
            )
    }

    private fun onEmptyResponse() {
        TODO("Not yet implemented")
    }

    private fun onEmptyData() {
        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(
                Intent(DETAILS_INTENT_FILTER)
                    .putExtra(RESULT_EXTRA, ERROR_EMPTY_DATA_RESULT)
            )
    }

    private fun onEmptyIntent() {
        TODO("Not yet implemented")
    }
}