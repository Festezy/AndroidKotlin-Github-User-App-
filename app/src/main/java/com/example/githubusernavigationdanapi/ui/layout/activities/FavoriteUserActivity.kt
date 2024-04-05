package com.example.githubusernavigationdanapi.ui.layout.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernavigationdanapi.databinding.ActivityFavoriteUserBinding
import com.example.githubusernavigationdanapi.ui.adapter.FavoriteUserAdapter
import com.example.githubusernavigationdanapi.ui.viewmodels.FavoriteUserViewModel
import com.example.githubusernavigationdanapi.helper.ViewModelFactory
import com.example.githubusernavigationdanapi.data.local.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.data.local.preferences.dataStore

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        favoriteUserViewModel = obtainViewModel(this@FavoriteUserActivity)
//        val favoriteViewModel = obtainViewModel(this@FavoriteUserActivity)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        val favoriteUserViewModel by viewModels<FavoriteUserViewModel>(){
            ViewModelFactory.getInstance(application, preferences)
        }

        favoriteUserViewModel.getAllFavoriteUsers().observe(this) {
            if (it != null) {
                adapter.setListFavoriteUser(it)
            }
        }

        adapter = FavoriteUserAdapter()
//        favoriteUserViewModel.getAllFavoriteUsers().observe(this) { users ->
//            val items = arrayListOf<ItemsItem>()
//            users.map {
//                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
//                items.add(item)
//            }
//            adapter.setListFavoriteUser(items)
//        }

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter
    }

//    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
//        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
//    }
}