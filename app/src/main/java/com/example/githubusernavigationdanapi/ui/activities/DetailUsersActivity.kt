package com.example.githubusernavigationdanapi.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusernavigationdanapi.R
import com.example.githubusernavigationdanapi.data.response.DetailUserResponse
import com.example.githubusernavigationdanapi.data.retrofit.ApiConfig
import com.example.githubusernavigationdanapi.ui.adapter.SectionsPagerAdapter
import com.example.githubusernavigationdanapi.databinding.ActivityDetailUsersBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUsersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUsersBinding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
        const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val username = intent.getStringExtra(EXTRA_USERNAME)
        getUserDetail(username)
    }

    private fun getUserDetail(username: String?) {
        val client = ApiConfig.getApiService().getDetailUser(username!!)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("DetailUsersActivity", "isSuccessful: ${response.body()}")
                    setDataUser(response.body())
                } else {
                    Log.d("DetailUsersActivity", "isfail: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.d("DetailUsersActivity", "onFailure: ${t.message}")
            }
        })
    }

    private fun setDataUser(userData: DetailUserResponse?) {
        with(binding) {
            Glide.with(this@DetailUsersActivity).load(userData!!.avatarUrl).into(profileImage)
            tvName.text = userData.name
            tvUsername.text = userData.login
            tvFollowers.text = userData.followers.toString()
            tvFollowing.text = userData.following.toString()
        }
    }
}