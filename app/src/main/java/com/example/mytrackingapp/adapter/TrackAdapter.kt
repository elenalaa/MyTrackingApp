package com.example.mytrackingapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mytrackingapp.R
import com.example.mytrackingapp.database.Track
import java.text.SimpleDateFormat
import java.util.*



class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>(){

    private lateinit var dateTV : TextView
    lateinit var avgSpeedTV : TextView
    lateinit var distanceTV : TextView
    lateinit var timeTV : TextView
    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindTrack(track: Track){
            with(track){
                dateTV = itemView.findViewById(R.id.dateTV)
                dateTV.text = track.timestamp.toString()

                timeTV = itemView.findViewById(R.id.timeTV)
                dateTV.text = track.timestamp.toString()

                avgSpeedTV = itemView.findViewById(R.id.avgSpeedTV)
                avgSpeedTV.text = track.avgSpeedKMH.toString()

                distanceTV = itemView.findViewById(R.id.distanceTV)
                avgSpeedTV.text = track.avgSpeedKMH.toString()
            }
        }
    }

    //Update new items
    val diffCallback = object : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    //Update Recycler view
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
        val track = differ.currentList[position]


        holder.itemView.apply {

            val calendar = Calendar.getInstance().apply {
                timeInMillis = track.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

            dateTV.text = dateFormat.format(calendar.time)

            val avgSpeed = "${track.avgSpeedKMH}km/h"
            avgSpeedTV.text = avgSpeed

            val distance = "${track.distance / 1000f}km"
            distanceTV.text = distance

        }
    }
}
