package com.example.githubusernavigationdanapi.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.example.githubusernavigationdanapi.data.remote.retrofit.ApiConfig
import com.example.githubusernavigationdanapi.data.local.preferences.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SettingPreferences): ViewModel() {
    private val _getUserData = MutableLiveData<List<ItemsItem?>?>()
    val getUserData: LiveData<List<ItemsItem?>?> = _getUserData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData<String>()
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    fun fetchData() {
        // Retrieve data based on the current search query
        viewModelScope.launch {
            _searchQuery.value?.let { username ->
                getUser(username)
            }
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    init {
        fetchData()
    }
    private suspend fun getUser(username: String) {
        _isLoading.value = true
        try {
            val responseCall = ApiConfig.getApiService().getUsers(username)
            _getUserData.value = responseCall.body()!!.items
        } catch (e: Exception){
            // Tangani error di sini
            Log.e("MainViewModel", "Error fetching user data: ${e.message}")
            // Misalnya, Anda dapat menetapkan data yang sesuai ke _getUserData
            _getUserData.value = emptyList()
        }
        _isLoading.value = false
//        val responseCall = ApiConfig.getApiService().getUsers(username)
        // change Call<GithubResponse> in interface fun getUsers
//        responseCall.enqueue(object : Callback<GithubResponse> {
//            override fun onResponse(
//                call: Call<GithubResponse>,
//                response: Response<GithubResponse>
//            ) {
//                if (response.isSuccessful) {
//                    _isLoading.value = false
//                    Log.d("MainActivity", "isSuccessful: ${response.body()}")
////                    setUserData(response.body()!!.items)
//                    _getUserData.value = response.body()!!.items
//                } else {
//                    Log.d("MainActivity", "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
//                Log.d("MainActivity", "onFailure: ${t.message}")
//                val context = MainActivity()
//                Toast.makeText(context.applicationContext, "onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }
}