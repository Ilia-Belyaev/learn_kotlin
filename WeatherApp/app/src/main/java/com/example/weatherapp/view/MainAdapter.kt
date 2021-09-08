package com.example.weatherapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.model.Weather

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    var weatherData: List<Weather> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: OnItemViewClickListener? = null

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.cityName).text = weather.city.city
                setOnClickListener {
                    listener?.onItemClick(weather)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder =
        MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_fragment_item, parent, false)
        )

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int = weatherData.size

    fun interface OnItemViewClickListener {
        fun onItemClick(weather: Weather)
    }
}

