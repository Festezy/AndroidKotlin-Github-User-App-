package com.example.githubusernavigationdanapi.ui.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubusernavigationdanapi.data.response.GithubResponse
import com.example.githubusernavigationdanapi.data.response.ItemsItem
import com.example.githubusernavigationdanapi.data.retrofit.ApiConfig
import com.example.githubusernavigationdanapi.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.ui.activities.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        _searchQuery.value?.let { username ->
            getUser(username)
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }

    init {
        fetchData()
    }
    private fun getUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    Log.d("MainActivity", "isSuccessful: ${response.body()}")
//                    setUserData(response.body()!!.items)
                    _getUserData.value = response.body()!!.items
                } else {
                    Log.d("MainActivity", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                Log.d("MainActivity", "onFailure: ${t.message}")
                val context = MainActivity()
                Toast.makeText(context.applicationContext, "onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}