package com.example.githubusernavigationdanapi.ui.layout.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernavigationdanapi.data.remote.response.ItemsItem
import com.example.githubusernavigationdanapi.databinding.FragmentFollowBinding
import com.example.githubusernavigationdanapi.ui.adapter.FollowAdapter
import com.example.githubusernavigationdanapi.ui.viewmodels.FollowFragmentViewModel
class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followFragmentViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[FollowFragmentViewModel::class.java]

        var username: String? = null
        var position: Int?  = null

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        followFragmentViewModel.setSearchQuery(username!!)
        followFragmentViewModel.fetchData()

        followFragmentViewModel.isLoading.observe(requireActivity()){
            showLoading(it)
        }

        if (position == 1){
            followFragmentViewModel.getUserFollowers.observe(requireActivity()){
                setUserFollows(it)
            }
        } else {
            followFragmentViewModel.getUserFollowing.observe(requireActivity()){
                setUserFollows(it)
            }
        }
    }

    private fun setUserFollows(items: List<ItemsItem?>?) {

        val adapter = FollowAdapter()
        adapter.submitList(items)
        with(binding){
            rvFollows.layoutManager= LinearLayoutManager(requireActivity())
            rvFollows.adapter = adapter
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
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "extra_username"
    }
}