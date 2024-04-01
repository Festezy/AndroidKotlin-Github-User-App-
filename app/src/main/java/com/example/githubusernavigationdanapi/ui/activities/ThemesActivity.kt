package com.example.githubusernavigationdanapi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.databinding.ActivityThemesBinding
import com.example.githubusernavigationdanapi.helper.ThemeViewModelFactory
import com.example.githubusernavigationdanapi.helper.ViewModelFactory
import com.example.githubusernavigationdanapi.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.preferences.dataStore
import com.example.githubusernavigationdanapi.ui.viewmodels.ThemesViewModel

class ThemesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThemesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        val themesViewModel = ViewModelProvider(this@ThemesActivity,
            ViewModelFactory(application, preferences)
        )[ThemesViewModel::class.java]

        with(binding) {
            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }

                themesViewModel.getThemeSettings().observe(this@ThemesActivity) { isDarkModeActive: Boolean ->
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        switchTheme.isChecked = true
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        switchTheme.isChecked = false
                    }
                }

                switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                    themesViewModel.saveThemeSetting(isChecked)
                }
            }
        }
    }
}