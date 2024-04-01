package com.example.githubusernavigationdanapi.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubusernavigationdanapi.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.ui.viewmodels.DetailUserViewModel
import com.example.githubusernavigationdanapi.ui.viewmodels.FavoriteUserViewModel
import com.example.githubusernavigationdanapi.ui.viewmodels.MainViewModel
import com.example.githubusernavigationdanapi.ui.viewmodels.ThemesViewModel

class ViewModelFactory(
    private val mApplication: Application,
    private var preferences: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(application: Application, preferences: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, preferences)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
//
//    private lateinit var preferences: SettingPreferences
    fun getPreferences(preferences: SettingPreferences) {
        this.preferences = preferences
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemesViewModel::class.java)){
            return ThemesViewModel(preferences) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(preferences) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(mApplication, preferences) as T
        } else if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}