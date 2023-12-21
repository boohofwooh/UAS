package com.example.uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.data.Station
import com.example.uas.databinding.ItemListStationBinding

// Import statements...

class StationAdapter(private val onDelete: (Station) -> Unit, private val onEdit: (Station) -> Unit) :
    ListAdapter<Station, StationAdapter.ItemStationViewHolder>(StationDiffCallback()) {

    inner class ItemStationViewHolder(private val binding: ItemListStationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Station) {
            with(binding) {
                station.text = data.itstation

                btnDelete.setOnClickListener {
                    onDelete(data)
                }

                btnEdit.setOnClickListener {
                    onEdit(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemStationViewHolder {
        val binding = ItemListStationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemStationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemStationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StationDiffCallback : DiffUtil.ItemCallback<Station>() {
        // Determine which items require updating in the RecyclerView.
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem == newItem
        }
    }
}
