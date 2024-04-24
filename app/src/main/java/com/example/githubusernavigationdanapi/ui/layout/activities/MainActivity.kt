package com.example.githubusernavigationdanapi.ui.layout.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.example.githubusernavigationdanapi.databinding.ActivityMainBinding
import com.example.githubusernavigationdanapi.helper.ViewModelFactory
import com.example.githubusernavigationdanapi.data.local.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.data.local.preferences.dataStore
import com.example.githubusernavigationdanapi.ui.adapter.UserAdapter
import com.example.githubusernavigationdanapi.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private var menu: Menu? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(700)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        obtainMainViewModel()
        setupViews()
        observeViewModel()

    }

    private fun obtainMainViewModel() {
        val preferences = SettingPreferences.getInstance(application.dataStore)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(application, preferences)
        )[MainViewModel::class.java]
    }

    private fun setupViews() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchView.hide()
                // mengambil string dari text yang di input di search bar/view dan mengambil List User
                mainViewModel.inputSearchQuery(searchView.text.toString())
                mainViewModel.fetchUserList()
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
        }
    }

    private fun observeViewModel() {
        mainViewModel.isLoading.observe(this@MainActivity) {
            showLoading(it)
        }
        mainViewModel.getThemeSettings()
            .observe(this@MainActivity) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.switchTheme.isChecked = false
                    this@MainActivity.menu?.findItem(R.id.Favorite)
                        ?.setIcon(R.drawable.baseline_favorite_24_white)
                    this@MainActivity.menu?.findItem(R.id.themes)
                        ?.setIcon(R.drawable.baseline_settings_24)
                }
            }
        lifecycleScope.launch {
            mainViewModel.getUserListMSFlow.collectLatest {
                setUsersData(it)
            }
        }
    }

    private fun setUsersData(items: List<ItemsItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(items)
        with(binding) {
            rvSearchUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvSearchUser.adapter = adapter
            rvSearchUser.setHasFixedSize(true)
        }
        if (items!!.isNotEmpty()) {
            Snackbar.make(binding.root, "Result ${items.size}", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}