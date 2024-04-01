package com.example.githubusernavigationdanapi.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusernavigationdanapi.data.response.ItemsItem
import com.example.githubusernavigationdanapi.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.databinding.ItemFavoriteUserBinding
import com.example.githubusernavigationdanapi.helper.NoteDiffCallback
import com.example.githubusernavigationdanapi.ui.activities.DetailUserActivity

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {
    private val listFavoriteUserEntity = ArrayList<FavoriteUserEntity>()
    fun setListFavoriteUser(listFavoriteUserEntity: List<FavoriteUserEntity>) {
        val diffCallback = NoteDiffCallback(this.listFavoriteUserEntity, listFavoriteUserEntity)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUserEntity.clear()
        this.listFavoriteUserEntity.addAll(listFavoriteUserEntity)
        diffResult.dispatchUpdatesTo(this)
    }
    inner class FavoriteUserViewHolder(private val binding: ItemFavoriteUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUserEntity: FavoriteUserEntity) {
            with(binding) {
                tvName.text = favoriteUserEntity.username
                Glide.with(itemView.context).load(favoriteUserEntity.avatarUrl).into(binding.imgPhoto)
                binding.cardView.setOnClickListener {
                    val intent = Intent(it.context, DetailUserActivity::class.java)
                    intent.putExtra(DetailUserActivity.EXTRA_USERNAME, favoriteUserEntity.username)
                    intent.putExtra(DetailUserActivity.EXTRA_URL, favoriteUserEntity.avatarUrl)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val binding = ItemFavoriteUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavoriteUserEntity.size

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(listFavoriteUserEntity[position])
    }
}