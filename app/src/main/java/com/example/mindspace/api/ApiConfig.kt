package com.example.mindspace.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://34.124.188.31:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

}