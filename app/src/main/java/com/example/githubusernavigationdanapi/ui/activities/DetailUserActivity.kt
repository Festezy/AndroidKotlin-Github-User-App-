package com.example.githubusernavigationdanapi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.data.response.DetailUserResponse
import com.example.githubusernavigationdanapi.database.FavoriteUser
import com.example.githubusernavigationdanapi.ui.adapter.SectionsPagerAdapter
import com.example.githubusernavigationdanapi.databinding.ActivityDetailUsersBinding
import com.example.githubusernavigationdanapi.ui.viewmodels.DetailUserViewModel
import com.example.githubusernavigationdanapi.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUsersBinding

    private var favoriteUser: FavoriteUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailUserViewModel::class.java]
        val detailUserViewModel = obtainViewModel(this@DetailUserActivity)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        detailUserViewModel.setSearchQuery(username!!)
        detailUserViewModel.fetchData()

        detailUserViewModel.getUserData.observe(this@DetailUserActivity){
            setDataUser(it)
        }

        detailUserViewModel.isLoading.observe(this@DetailUserActivity){
            showLoading(it)
        }

        favoriteUser = FavoriteUser()

        binding.fabAdd.setOnClickListener{

            favoriteUser.let {favoriteUser ->
                favoriteUser!!.username = username
                favoriteUser.avatarUrl = avatarUrl
            }
            detailUserViewModel.insert(favoriteUser as FavoriteUser)
            Toast.makeText(this, "Favorite ditambahkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

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
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_URL = "extra_url"
    }
}