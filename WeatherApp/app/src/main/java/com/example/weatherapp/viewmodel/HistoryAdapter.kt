package com.example.weatherapp.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.model.database.HistoryEntity
import com.example.weatherapp.view.R
import kotlinx.android.synthetic.main.detail_fragment.view.*
import kotlinx.android.synthetic.main.history_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<HistoryEntity> = arrayListOf()

    fun setData(data: List<HistoryEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data: HistoryEntity) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                with(itemView){
                    id_history.text = data.id.toString()
                    city.text = data.city
                    temperature.text = data.temperature.toString()
                    date.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(data.timestamp))
                }
            }
        }
    }
}

