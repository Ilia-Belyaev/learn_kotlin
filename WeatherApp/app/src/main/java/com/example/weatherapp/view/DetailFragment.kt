package com.example.weatherapp.view

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.WeatherDTO
import com.example.weatherapp.model.WeatherLoader
import com.example.weatherapp.view.databinding.DetailFragmentBinding
import com.example.weatherapp.view.databinding.DetailFragmentBinding.inflate
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URI
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class DetailFragment : Fragment() {
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment =
            DetailFragment().apply { arguments = bundle }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>(WEATHER_EXTRA)?.let {
            it.city.also { city ->
                binding.cityName.text = city.city
                binding.cityCoordinates.text = String.format(
                    getString(R.string.city_coordinates),
                    city.latitude.toString(),
                    city.longitude.toString()
                )
            }
            WeatherLoader(
                it.city.latitude,
                it.city.longitude,
                object : WeatherLoader.WeatherLoaderListener {
                    override fun onLoaded(weatherDTO: WeatherDTO) {
                        requireActivity().runOnUiThread {
                            displayWeather(weatherDTO)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                throwable.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }).goToInternet()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayWeather(weather: WeatherDTO) {
        with(binding) {
            temperatureValue.text = weather.fact?.temp.toString()
            feelsLikeValue.text = weather.fact?.feels_like.toString()
            weatherCondition.text = weather.fact?.condition.toString()
        }
    }


}