package com.example.weatherapp.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.FrameLayout
import com.example.weatherapp.model.Weather
import com.example.weatherapp.view.databinding.MainFragmentBinding
import com.example.weatherapp.viewmodel.AppState
import com.example.weatherapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter: MainAdapter by lazy {
        MainAdapter()
    }
    private var isRus: Boolean = true

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FrameLayout {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.listener = MainAdapter.OnItemViewClickListener { weather ->
            activity?.supportFragmentManager?.let {
                it.beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance(Bundle().apply {
                        putParcelable(DetailFragment.WEATHER_EXTRA, weather)
                    }))
                    .addToBackStack("").commit()
            }
        }
        binding.apply {
            recyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener {
                isRus = !isRus
                with(binding.mainFragmentFAB) {
                    val icId = if (isRus) R.drawable.ic_russia else R.drawable.ic_earth
                    setImageResource(icId)
                }
                viewModel.getWeatherFromLocalSource(isRus)
            }
        }
        viewModel.apply {
            LiveData.observe(viewLifecycleOwner) { data ->
                renderData(data)
            }
            getWeatherFromLocalSource(isRus)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(data: AppState?) {
        when (data) {
            is AppState.Loading -> binding.loadingLayout.show()
            is AppState.Success -> {
                binding.loadingLayout.hide()
                adapter.weatherData = data.weather
            }
            is AppState.Error -> {
                binding.loadingLayout.hide()
                binding.mainFragmentFAB.showSnackBar(
                    "Error",
                    "Reload",
                    { viewModel.getWeatherFromLocalSource(isRus) })
            }

        }
    }

}