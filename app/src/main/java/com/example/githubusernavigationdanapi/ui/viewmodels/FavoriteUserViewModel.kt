package com.example.githubusernavigationdanapi.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernavigationdanapi.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllNotes(): LiveData<List<FavoriteUserEntity>> = mFavoriteUserRepository.getAllNotes()

}