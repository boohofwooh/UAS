package com.example.uas

import android.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.data.Trip
import com.example.uas.databinding.ItemPerjalananBinding

typealias OnClickTrip = (Trip) -> Unit

class TripAdapter(private val onClickTrip: OnClickTrip, private val onDelete: (Trip) -> Unit, private val onEdit: (Trip) -> Unit) :
    ListAdapter<Trip, TripAdapter.ItemPerjalananViewHolder>(TripDiffCallback()) {
    inner class ItemPerjalananViewHolder(private val binding: ItemPerjalananBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val listPktContainer: LinearLayout = binding.listPkt

        fun bind(data: Trip) {
            with(binding) {
                tglKeberangkatan.text = data.tgl_brgkt
                tglKembali.text = data.tgl_kembali
                stasiunAsal.text = data.st_awal
                stasiunTujuan.text = data.st_tujuan
                kelas.text = data.kelas
                harga.text = data.harga
                kursi.text = data.kursi
                listPktContainer.removeAllViews()

                for (paket in data.list_paket) {
                    val textView = TextView(listPktContainer.context)
                    textView.text = paket
                    listPktContainer.addView(textView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPerjalananViewHolder {
        val binding = ItemPerjalananBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemPerjalananViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemPerjalananViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TripDiffCallback : DiffUtil.ItemCallback<Trip>() {
        //        menentukan perubahan item mana yang diperlukan untuk diperbarui di dalam RecyclerView.
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem == newItem
        }
    }
}