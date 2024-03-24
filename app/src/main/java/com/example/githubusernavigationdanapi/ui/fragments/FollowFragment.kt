package com.example.githubusernavigationdanapi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernavigationdanapi.data.response.ItemsItem
import com.example.githubusernavigationdanapi.data.retrofit.ApiConfig
import com.example.githubusernavigationdanapi.databinding.FragmentFollowBinding
import com.example.githubusernavigationdanapi.ui.adapter.FollowAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username: String? = null
        var position: Int?  = null

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1){
            getFollowers(username!!)
        } else {
            getFollowing(username!!)
        }
    }

    private fun getFollowing(username: String) {
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                setUserFollows(response.body()!!)
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getFollowers(username: String) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                setUserFollows(response.body()!!)
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setUserFollows(items: List<ItemsItem?>?) {

        val adapter = FollowAdapter(requireActivity())
        adapter.submitList(items)
        with(binding){
            rvFollows.layoutManager= LinearLayoutManager(requireActivity())
            rvFollows.adapter = adapter
            rvFollows.setHasFixedSize(true)
        }
    }

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "extra_username"
    }
}