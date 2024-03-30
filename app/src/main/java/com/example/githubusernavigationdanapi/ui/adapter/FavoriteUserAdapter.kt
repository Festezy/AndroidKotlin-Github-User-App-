package com.example.githubusernavigationdanapi.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusernavigationdanapi.database.FavoriteUser
import com.example.githubusernavigationdanapi.databinding.ItemFavoriteUserBinding
import com.example.githubusernavigationdanapi.helper.NoteDiffCallback

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {
    private val listFavoriteUser = ArrayList<FavoriteUser>()
    fun setListNotes(listFavoriteUser: List<FavoriteUser>) {
        val diffCallback = NoteDiffCallback(this.listFavoriteUser, listFavoriteUser)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavoriteUser.clear()
        this.listFavoriteUser.addAll(listFavoriteUser)
        diffResult.dispatchUpdatesTo(this)
    }
    inner class FavoriteUserViewHolder(private val binding: ItemFavoriteUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            with(binding) {
                tvName.text = favoriteUser.username
//                imgPhoto.setImageURI(Uri.parse(favoriteUser.avatarUrl))
                Glide.with(itemView.context).load(favoriteUser.avatarUrl).into(binding.imgPhoto)
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

    override fun getItemCount(): Int = listFavoriteUser.size

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position])
    }
}