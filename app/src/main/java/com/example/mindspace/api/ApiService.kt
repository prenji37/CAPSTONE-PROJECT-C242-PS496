package com.example.mindspace.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun registerUser(
        @Body
        request: RegisterRequest
    ): Response<Unit>

    @POST("login")
    suspend fun loginUser(
        @Body
        request: LoginRequest
    ): Response<Unit>
}
