package com.example.uas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.data.Users
import com.example.uas.databinding.ItemListAdminBinding
import com.google.firebase.firestore.auth.User

class AdminAdapter(private val onDelete: (Users) -> Unit, private val onEdit: (Users) -> Unit) :
    ListAdapter<Users, AdminAdapter.ItemAdminViewHolder>(AdminDiffCallback()) {

    inner class ItemAdminViewHolder(private val binding: ItemListAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Users) {
            with(binding) {
                username.text = data.username
                email.text = data.email
                phone.text = data.phone
                password.text = data.password

                btnDelete.setOnClickListener {
                    onDelete(data)
                }

                btnEdit.setOnClickListener {
                    onEdit(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdminViewHolder {
        val binding = ItemListAdminBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ItemAdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemAdminViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AdminDiffCallback : DiffUtil.ItemCallback<Users>() {
        //        menentukan perubahan item mana yang diperlukan untuk diperbarui di dalam RecyclerView.
        override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem == newItem
        }
    }
}