package com.example.githubusernavigationdanapi.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        fun getApiService(): ApiService {
//            val loggingInterceptor = if(BuildConfig.DEBUG) {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            } else {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
//            }

            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "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")
                    .build()
                chain.proceed(requestHeaders)
            }

            val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

//            val client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor).build()

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}