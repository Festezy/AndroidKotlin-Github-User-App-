package com.example.githubusernavigationdanapi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.data.response.DetailUserResponse
import com.example.githubusernavigationdanapi.ui.adapter.SectionsPagerAdapter
import com.example.githubusernavigationdanapi.databinding.ActivityDetailUsersBinding
import com.example.githubusernavigationdanapi.ui.viewmodels.DetailUserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailUserViewModel::class.java]

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailUserViewModel.setSearchQuery(username!!)
        detailUserViewModel.fetchData()

        detailUserViewModel.getUserData.observe(this@DetailUserActivity){
            setDataUser(it)
        }

        detailUserViewModel.isLoading.observe(this@DetailUserActivity){
            showLoading(it)
        }
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
    }
}