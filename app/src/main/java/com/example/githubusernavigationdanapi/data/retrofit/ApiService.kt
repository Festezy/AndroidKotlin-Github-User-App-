package com.example.githubusernavigationdanapi.data.retrofit

import com.example.githubusernavigationdanapi.data.response.DetailUserResponse
import com.example.githubusernavigationdanapi.data.response.GithubResponse
import com.example.githubusernavigationdanapi.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
//    @GET("search/users")
//    @Headers("Authorization: token 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")
//    fun getUsers(
//        @Query("q") username: String
//    ): Call<GithubResponse>

    @GET("search/users")
    fun getUsers(
        @Query("q") username: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}