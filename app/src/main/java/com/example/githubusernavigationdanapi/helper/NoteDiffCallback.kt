package com.example.githubusernavigationdanapi.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubusernavigationdanapi.data.local.database.FavoriteUserEntity

class NoteDiffCallback(private val oldFavoriteUserEntityList: List<FavoriteUserEntity>, private val newFavoriteUserEntityList: List<FavoriteUserEntity>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteUserEntityList.size

    override fun getNewListSize(): Int = newFavoriteUserEntityList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteUserEntityList[oldItemPosition] == newFavoriteUserEntityList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldFavoriteUserEntityList[oldItemPosition]
        val newNote = newFavoriteUserEntityList[newItemPosition]
        return oldNote.username == newNote.username && oldNote.avatarUrl == newNote.avatarUrl
    }
}