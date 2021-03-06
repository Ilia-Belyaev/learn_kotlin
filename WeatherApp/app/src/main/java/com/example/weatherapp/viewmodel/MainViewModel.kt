package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.RepoImpl
import com.example.weatherapp.model.Weather

class MainViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> =
        MutableLiveData()// в viewmodel оперируем изменяемой livedata, в activity наоборот
    val LiveData: LiveData<AppState> = liveDataToObserve
    private val repoImpl: Repo = RepoImpl()

    fun getLiveData() = liveDataToObserve
    fun getWeatherFromLocalSource(isRussian: Boolean) = getDataFromLocalSource(isRussian)

    private fun getDataFromLocalSource(isRussian: Boolean = true) {
        liveDataToObserve.value = AppState.Loading

            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian) {
                        repoImpl.getWeatherFromLocalStorageRus()
                    } else {
                        repoImpl.getWeatherFromLocalStorageWorld()
                    }
                )
            )

    }


}