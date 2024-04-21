package com.example.githubusernavigationdanapi.ui.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusernavigationdanapi.data.remote.response.DetailUserResponse
import com.example.githubusernavigationdanapi.data.remote.retrofit.ApiConfig
import com.example.githubusernavigationdanapi.data.local.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.data.local.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.data.local.repository.FavoriteUserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application, private val preferences: SettingPreferences): ViewModel() {
    private val _getUserDetail = MutableLiveData<DetailUserResponse>()
    val getUserDetail: LiveData<DetailUserResponse> = _getUserDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData<String>()
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun fetchData() {
        // Retrieve data based on the current search query
        viewModelScope.launch{
            _searchQuery.value?.let { username ->
                getUserDetail(username)
            }
        }
    }

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

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

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    init {
        fetchData()
    }
    private suspend fun getUserDetail(username: String?) {
        _isLoading.value = true
        val response = ApiConfig.getApiService().getDetailUser(username!!)
        try{
            _getUserDetail.value = response.body()
        } catch(e: Exception){
            Log.d("MainViewModel", e.message.toString())
//            _getUserDetail.value = e.message.toString
        }
        _isLoading.value = false
//        val client = ApiConfig.getApiService().getDetailUser(username!!)
//        client.enqueue(object : Callback<DetailUserResponse> {
//            override fun onResponse(
//                call: Call<DetailUserResponse>,
//                response: Response<DetailUserResponse>
//            ) {
//                if (response.isSuccessful) {
//                    _isLoading.value = false
//                    Log.d("DetailUsersViewModel", "isSuccessful: ${response.body()}")
//                    _getUserDetail.value = response.body()
//                } else {
//                    Log.d("DetailUsersViewModel", "isFailing: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
//                Log.d("DetailUsersViewModel", "onFailure: ${t.message}")
//            }
//        })
    }
}