package com.example.uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.databinding.ItemWishlistBinding

typealias OnClickClass = (wTrip) -> Unit

class wTripAdapter(
    private val onClickClass: OnClickClass,
    private val onDelete: (wTrip) -> Unit,
    private val onEdit: (wTrip) -> Unit
) : ListAdapter<wTrip, wTripAdapter.ItemTripViewHolder>(TripDiffCallback()) {

    inner class ItemTripViewHolder(private val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: wTrip) {
            with(binding) {
                tglKeberangkatan.text = data.tgl_brgkt
                tglKembali.text = data.tgl_kembali
                stasiunAsal.text = data.st_awal
                stasiunTujuan.text = data.st_tujuan

                btnDelete.setOnClickListener {
                    onDelete(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTripViewHolder {
        val binding = ItemWishlistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemTripViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemTripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TripDiffCallback : DiffUtil.ItemCallback<wTrip>() {
        override fun areItemsTheSame(oldItem: wTrip, newItem: wTrip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: wTrip, newItem: wTrip): Boolean {
            return oldItem == newItem
        }
    }
}
