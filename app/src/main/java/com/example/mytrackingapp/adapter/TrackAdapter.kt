package com.example.mytrackingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytrackingapp.R
import com.example.mytrackingapp.database.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>(){
    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    val diffCallback = object : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Track>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder{
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.simple_track,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timestamp
            }
            /*val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            val avgSpeed = "${run.avgSpeed}km/h"
            tvAvgSpeed.text = avgSpeed

            val distance = "${run.distance / 1000f}km"
            tvDistance.text = distance

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(track.timeInMillis)*/

        }
    }
}
