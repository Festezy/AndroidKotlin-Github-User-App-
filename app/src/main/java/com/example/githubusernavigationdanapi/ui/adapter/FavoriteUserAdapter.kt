package com.example.githubusernavigationdanapi.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusernavigationdanapi.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.databinding.ItemFavoriteUserBinding
import com.example.githubusernavigationdanapi.helper.NoteDiffCallback

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {
    private val listFavoriteUserEntity = ArrayList<FavoriteUserEntity>()
    fun setListNotes(listFavoriteUserEntity: List<FavoriteUserEntity>) {
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
//                imgPhoto.setImageURI(Uri.parse(favoriteUser.avatarUrl))
                Glide.with(itemView.context).load(favoriteUserEntity.avatarUrl).into(binding.imgPhoto)
//                cvItemNote.setOnClickListener {
//                    val intent = Intent(it.context, NoteAddUpdateActivity::class.java)
//                    intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
//                    it.context.startActivity(intent)
//                }
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