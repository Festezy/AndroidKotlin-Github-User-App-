package com.example.githubusernavigationdanapi.ui.layout.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.data.remote.response.DetailUserResponse
import com.example.githubusernavigationdanapi.data.local.database.FavoriteUserEntity
import com.example.githubusernavigationdanapi.ui.adapter.SectionsPagerAdapter
import com.example.githubusernavigationdanapi.databinding.ActivityDetailUsersBinding
import com.example.githubusernavigationdanapi.ui.viewmodels.DetailUserViewModel
import com.example.githubusernavigationdanapi.helper.ViewModelFactory
import com.example.githubusernavigationdanapi.data.local.preferences.SettingPreferences
import com.example.githubusernavigationdanapi.data.local.preferences.dataStore
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUsersBinding

    private var favoriteUserEntity: FavoriteUserEntity? = null

    private var isUserFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailUserViewModel::class.java]
//        val detailUserViewModel = obtainViewModel(this@DetailUserActivity)

        val preferences = SettingPreferences.getInstance(application.dataStore)
        val detailUserViewModel by viewModels<DetailUserViewModel>{
            ViewModelFactory.getInstance(application, preferences)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        // merubah text color tablayout
        detailUserViewModel.getThemeSettings().observe(this@DetailUserActivity){ isDarkModeActive->
            val normalTextColor = if (isDarkModeActive) {
                // Warna teks untuk tema gelap
                ContextCompat.getColor(this, R.color.white)
            } else {
                // Warna teks untuk tema terang
                ContextCompat.getColor(this, R.color.black)
            }

            val selectedTextColor = if (isDarkModeActive) {
                // Warna teks terpilih untuk tema gelap
                ContextCompat.getColor(this, R.color.white)
            } else {
                // Warna teks terpilih untuk tema terang
                ContextCompat.getColor(this, R.color.black)
            }

            // Set tab text colors
            tabs.setTabTextColors(normalTextColor, selectedTextColor)
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        detailUserViewModel.setSearchQuery(username!!)
        detailUserViewModel.fetchData()

        detailUserViewModel.getUserDetail.observe(this@DetailUserActivity) {
            setDataUser(it)

        }

        detailUserViewModel.isLoading.observe(this@DetailUserActivity) {
            showLoading(it)
        }

        favoriteUserEntity = FavoriteUserEntity()


        // Mendapatkan status apakah user sudah menjadi favorit atau belum
        // Check if user is favorite

        detailUserViewModel.isUserFavorite.observe(this@DetailUserActivity) {
            it.username = username
        }

        detailUserViewModel.getUserFavorite(username).observe(this@DetailUserActivity) {
            if (it != null) {
                isUserFavorite = true
                favoriteUserEntity!!.id = it.id
                binding.fabAdd.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabAdd.context,
                        R.drawable.baseline_favoritefull_24
                    )
                )
            } else {
                isUserFavorite = false
                binding.fabAdd.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabAdd.context,
                        R.drawable.baseline_favorite_border_24
                    )
                )
            }
        }

        binding.fabAdd.setOnClickListener {
            isUserFavorite = !isUserFavorite
            binding.fabAdd.apply {
                if (isUserFavorite){
                    favoriteUserEntity?.let { favorite ->
                        favorite.username = username
                        favorite.avatarUrl = avatarUrl
                        detailUserViewModel.insert(favorite)
                        Toast.makeText(
                            this@DetailUserActivity,
                            "Favorite ditambahkan",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                } else {
                    isUserFavorite = false
                    favoriteUserEntity?.let {
                        detailUserViewModel.delete(it)
                        Toast.makeText(
                            this@DetailUserActivity,
                            "Favorite dihapus",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    Toast.makeText(this@DetailUserActivity, "Favorite dihapus", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

//    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
//        val factory = ViewModelFactory.getInstance(activity.application)
//        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
//    }

    private fun setDataUser(userData: DetailUserResponse?) {
        with(binding) {
            Glide.with(this@DetailUserActivity).load(userData!!.avatarUrl).into(profileImage)
            tvName.text = userData.name
            tvUsername.text = userData.login
            tvFollowers.text = userData.followers.toString()
            tvFollowing.text = userData.following.toString()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_URL = "extra_url"
    }
}