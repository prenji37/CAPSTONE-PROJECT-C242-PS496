package com.example.mindspace.api

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String)

data class LoginRequest(
    val email: String,
    val password: String)

