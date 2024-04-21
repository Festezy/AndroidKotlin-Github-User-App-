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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SettingPreferences): ViewModel() {
    private val _searchQuery = MutableLiveData<String>()

//    private val _getUserData = MutableLiveData<List<ItemsItem?>?>()
//    val getUserData: LiveData<List<ItemsItem?>?> = _getUserData

    private val _getUserList = MutableStateFlow<List<ItemsItem>>(listOf())
    val getUserList = _getUserList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setSearchQuery(username: String) {
        _searchQuery.value = username
    }
    fun fetchData() =
        // Retrieve data based on the current search query
        viewModelScope.launch {
            _searchQuery.value?.let { username ->
                getUser(username)
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
//            _getUserData.value = responseCall.body()!!.items
            _getUserList.value = responseCall.body()!!.items!!
        } catch (e: Exception){
            // Tangani error di sini
            Log.e("MainViewModel", "Error fetching user data: ${e.message}")
            // Misalnya, Anda dapat menetapkan data yang sesuai ke _getUserData
//            _getUserData.value = emptyList()
            _getUserList.value = emptyList()
        }
        _isLoading.value = false
    }
}