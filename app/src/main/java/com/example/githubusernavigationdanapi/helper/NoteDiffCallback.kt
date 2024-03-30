package com.example.githubusernavigationdanapi.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubusernavigationdanapi.database.FavoriteUser

class NoteDiffCallback(private val oldFavoriteUserList: List<FavoriteUser>, private val newFavoriteUserList: List<FavoriteUser>) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteUserList.size

    override fun getNewListSize(): Int = newFavoriteUserList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteUserList[oldItemPosition] == newFavoriteUserList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldFavoriteUserList[oldItemPosition]
        val newNote = newFavoriteUserList[newItemPosition]
        return oldNote.username == newNote.username && oldNote.avatarUrl == newNote.avatarUrl
    }
}