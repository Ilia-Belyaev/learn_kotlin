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
    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MainAdapter
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
        adapter = MainAdapter()
        adapter.listener = MainAdapter.OnItemViewClickListener { weather ->
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle= Bundle()
                bundle.putParcelable(DetailFragment.WEATHER_EXTRA, weather)
                manager.beginTransaction()
                    .replace(R.id.container, DetailFragment.newInstance(bundle))
                    .addToBackStack("").commit()
            }
        }
        binding.recyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener {
            isRus = !isRus
            if (isRus) {
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            } else {
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            }
            viewModel.getWeatherFromLocalSource(isRus)
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.LiveData.observe(viewLifecycleOwner) { data ->
            renderData(data)
        }
        viewModel.getWeatherFromLocalSource(isRus)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

private fun renderData(data: AppState?) {
    when (data) {
        is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
        is AppState.Success -> {
            binding.loadingLayout.visibility = View.GONE
            adapter.weatherData = data.weather
        }
        is AppState.Error -> {
            binding.loadingLayout.visibility = View.GONE
            Snackbar
                .make(binding.mainFragmentFAB, "Error", Snackbar.LENGTH_INDEFINITE)
                .setAction("Reload") { viewModel.getWeatherFromLocalSource(isRus) }
                .show()
        }

    }
}

}