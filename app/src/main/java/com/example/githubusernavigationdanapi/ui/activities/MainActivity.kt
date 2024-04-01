package com.example.githubusernavigationdanapi.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.data.response.ItemsItem
import com.example.githubusernavigationdanapi.databinding.ActivityMainBinding
import com.example.githubusernavigationdanapi.helper.ViewModelFactory
import com.example.githubusernavigationdanapi.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.preferences.dataStore
import com.example.githubusernavigationdanapi.ui.adapter.UserAdapter
import com.example.githubusernavigationdanapi.ui.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val preferences = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(application, preferences)
        )[MainViewModel::class.java]


        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchView.hide()
                    Toast.makeText(this@MainActivity, searchView.text, Toast.LENGTH_SHORT).show()

                    mainViewModel.setSearchQuery(searchView.text.toString())
                    mainViewModel.fetchData()

                    mainViewModel.getUserData.observe(this@MainActivity) {
                        setUserData(it)
                    }

                    mainViewModel.isLoading.observe(this@MainActivity) {
                        showLoading(it)
                    }

                    false
                }
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.Favorite -> {
                        startActivity(Intent(this@MainActivity, FavoriteUserActivity::class.java))
                        true
                    }
                    R.id.themes -> {
                        startActivity(Intent(this@MainActivity, ThemesActivity::class.java))
                        true
                    }

                    else -> false
                }
            }

            mainViewModel.getThemeSettings().observe(this@MainActivity) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }
//
//            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
//                mainViewModel.saveThemeSetting(isChecked)
//            }

        }
    }

    private fun setUserData(items: List<ItemsItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(items)
        with(binding) {
            rvSearchUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvSearchUser.adapter = adapter
            rvSearchUser.setHasFixedSize(true)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}