package com.example.githubusernavigationdanapi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernavigationdanapi.data.response.ItemsItem
import com.example.githubusernavigationdanapi.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragmentViewModel: ViewModel() {
    private val _getUserFollowing = MutableLiveData<List<ItemsItem?>?>()
    val getUserFollowing: LiveData<List<ItemsItem?>?> = _getUserFollowing

    private val _getUserFollowers = MutableLiveData<List<ItemsItem?>?>()
    val getUserFollowers: LiveData<List<ItemsItem?>?> = _getUserFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData<String>()
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun fetchData() {
        // Retrieve data based on the current search query
        _searchQuery.value?.let { username ->
            getFollowers(username)
            getFollowing(username)
        }
    }

    init {
        fetchData()
    }

    private fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _getUserFollowing.value = response.body()
                    Log.d("FollowsViewModel", "isSuccessful: ${response.body()}")
                } else {
                    Log.d("FollowsViewModel", "isFailing: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.d("FollowsViewModel", "onFailure: ${t.message}")
            }
        })
    }

    private fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                _getUserFollowers.value = response.body()
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}