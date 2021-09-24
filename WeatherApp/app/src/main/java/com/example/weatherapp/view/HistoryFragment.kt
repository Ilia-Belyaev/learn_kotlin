package com.example.weatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.view.databinding.DetailFragmentBinding
import com.example.weatherapp.view.databinding.FragmentHistoryBinding
import com.example.weatherapp.viewmodel.DetailViewModel
import com.example.weatherapp.viewmodel.HistoryAdapter
import com.example.weatherapp.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val adapter:HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        _binding = FragmentHistoryBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter=adapter
        adapter.setData(viewModel.getAllHistory())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}