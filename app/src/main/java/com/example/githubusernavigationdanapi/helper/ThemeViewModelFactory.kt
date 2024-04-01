package com.example.githubusernavigationdanapi.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubusernavigationdanapi.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.ui.viewmodels.ThemesViewModel

class ThemeViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemesViewModel::class.java)) {
            return ThemesViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}