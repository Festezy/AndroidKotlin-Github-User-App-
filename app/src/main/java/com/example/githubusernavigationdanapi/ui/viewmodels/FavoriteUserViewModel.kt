package com.example.githubusernavigationdanapi.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernavigationdanapi.data.local.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.data.local.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUserEntity>> = mFavoriteUserRepository.getAllFavoriteUsers()

}