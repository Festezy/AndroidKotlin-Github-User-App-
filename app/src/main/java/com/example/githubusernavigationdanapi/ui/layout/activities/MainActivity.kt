package com.example.githubusernavigationdanapi.ui.layout.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.example.githubusernavigationdanapi.databinding.ActivityMainBinding
import com.example.githubusernavigationdanapi.helper.ViewModelFactory
import com.example.githubusernavigationdanapi.data.local.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.data.local.preferences.dataStore
import com.example.githubusernavigationdanapi.ui.adapter.UserAdapter
import com.example.githubusernavigationdanapi.ui.viewmodels.MainViewModel
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var menu: Menu? = null
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
                .setOnEditorActionListener { _, _, _ ->
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

            mainViewModel.getThemeSettings()
                .observe(this@MainActivity) { isDarkModeActive: Boolean ->
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        switchTheme.isChecked = true
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        switchTheme.isChecked = false
                        this@MainActivity.menu?.findItem(R.id.Favorite)
                            ?.setIcon(R.drawable.baseline_favorite_24_white)
                        this@MainActivity.menu?.findItem(R.id.themes)
                            ?.setIcon(R.drawable.baseline_settings_24)
                    }
                }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
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