package com.example.weatherapp.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.weatherapp.model.*
import com.example.weatherapp.view.databinding.DetailFragmentBinding
import com.example.weatherapp.viewmodel.AppState
import com.example.weatherapp.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.detail_fragment.*


class DetailFragment : Fragment() {

    companion object {
        const val WEATHER_EXTRA = "WEATHER_EXTRA"
        fun newInstance(bundle: Bundle): DetailFragment =
            DetailFragment().apply { arguments = bundle }
    }

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this).get(DetailViewModel::class.java)
    }
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.detail_fragment, container, false)
        _binding = DetailFragmentBinding.bind(view)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable<Weather>(WEATHER_EXTRA) ?: Weather()
        viewModel.liveData.observe(viewLifecycleOwner) {
            renderData(it)
        }
        viewModel.getWeatherFromRemoteSource(weather)
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.Success -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                val weather = data.weather.first()

                viewModel.saveWeather(weather)
                with(binding) {
                    imageView.load("https://www.freepngimg.com/thumb/city/36275-3-city-hd.png")
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    weatherCondition.text = weather.condition
                    cityName.text = weather.city.city
                    cityCoordinates.text = "${weather.city.latitude} ${weather.city.longitude}"
                }
            }
            is AppState.Error -> {

                binding.loadingLayout.show()
                binding.mainView.showSnackBar(
                    "Error",
                    "Reload",
                    { viewModel.getWeatherFromRemoteSource(Weather()) }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

