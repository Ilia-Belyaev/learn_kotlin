package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.io.IOException
import java.text.ParseException

const val MAIN_LINK = "https://api.weather.yandex.ru/v2/informers?"

class DetailViewModel : ViewModel() {
    private val repository: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
    private val detailLiveData = MutableLiveData<AppState>()

    val liveData: LiveData<AppState> = detailLiveData
    fun getWeatherFromRemoteSource(weather: Weather) {
        detailLiveData.value = AppState.Loading

        repository.getWeatherDetailFromServer(weather.city.latitude,
            weather.city.longitude,
            object : Callback<WeatherDTO> {
                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    detailLiveData.postValue(AppState.Error(t))
                }

                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    response.body()?.let {
                        detailLiveData.postValue(checkResponse(it))
                    }
                }

            })
    }

    private fun checkResponse(response: WeatherDTO): AppState {
        val factDTO = response.fact
        return if (factDTO?.condition != null
            && factDTO.condition.isNotBlank()
            && factDTO.temp != null
            && factDTO.feels_like != null
        ) {
            AppState.Success(
                listOf(
                    Weather(
                        temperature = factDTO.temp,
                        feelsLike = factDTO.feels_like,
                        condition = factDTO.condition
                    )
                )
            )
        } else {
            AppState.Error(ParseException("Do not parse Gson", 0))
        }
    }
}
