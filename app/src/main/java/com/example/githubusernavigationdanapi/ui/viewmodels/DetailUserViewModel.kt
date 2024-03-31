package com.example.githubusernavigationdanapi.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernavigationdanapi.data.response.DetailUserResponse
import com.example.githubusernavigationdanapi.data.retrofit.ApiConfig
import com.example.githubusernavigationdanapi.database.FavoriteUserDao
import com.example.githubusernavigationdanapi.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.database.FavoriteUserRoomDatabase
import com.example.githubusernavigationdanapi.repository.FavoriteUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application): ViewModel() {
    private val _getUserDetail = MutableLiveData<DetailUserResponse>()
    val getUserData: LiveData<DetailUserResponse> = _getUserDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData<String>()
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun fetchData() {
        // Retrieve data based on the current search query
        _searchQuery.value?.let { username ->
            getUserDetail(username)
        }
    }

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)
    private val mFavoriteDao: FavoriteUserDao = FavoriteUserRoomDatabase.getDatabase(application).favoriteUserDao()

    private val _isUserFavorite = MutableLiveData<FavoriteUserEntity>()
    val isUserFavorite: LiveData<FavoriteUserEntity> = _isUserFavorite
    fun insert(favoriteUserEntity: FavoriteUserEntity) {
        mFavoriteUserRepository.insert(favoriteUserEntity)
    }

    fun getUserFavorite(username: String): LiveData<FavoriteUserEntity> = mFavoriteUserRepository.getUserFavorite(
        username
    )

    fun delete(favoriteUserEntity: FavoriteUserEntity) {
//        mFavoriteUserRepository.delete(id)
        mFavoriteUserRepository.delete(favoriteUserEntity)
    }

    init {
        fetchData()
    }
    private fun getUserDetail(username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username!!)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    Log.d("DetailUsersViewModel", "isSuccessful: ${response.body()}")
                    _getUserDetail.value = response.body()
                } else {
                    Log.d("DetailUsersViewModel", "isFailing: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.d("DetailUsersViewModel", "onFailure: ${t.message}")
            }
        })
    }
}