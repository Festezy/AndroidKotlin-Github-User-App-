package com.example.githubusernavigationdanapi.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernavigationdanapi.data.response.DetailUserResponse
import com.example.githubusernavigationdanapi.data.retrofit.ApiConfig
import com.example.githubusernavigationdanapi.database.FavoriteUser
import com.example.githubusernavigationdanapi.repository.FavoriteUserRepository
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

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }
    //    fun update(note: Note) {
//        mNoteRepository.update(note)
//    }
//    fun delete(note: Note) {
//        mNoteRepository.delete(note)
//    }

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