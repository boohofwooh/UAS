package com.example.uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.databinding.ItemWishlistBinding

typealias OnClickTrips = (Trips) -> Unit
class TripsAdapter(private val onClickTrips: OnClickTrips, private val onDelete: (Trips) -> Unit, private val
wishlist: List<Trips>) :
    RecyclerView.Adapter<TripsAdapter.ItemWishlistViewHolder>(){
    inner class ItemWishlistViewHolder(private val binding: ItemWishlistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Trips) {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWishlistViewHolder {
        val binding = ItemWishlistBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ItemWishlistViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return wishlist.size
    }

    override fun onBindViewHolder(holder: ItemWishlistViewHolder, position: Int) {
        holder.bind(wishlist[position])
    }
}