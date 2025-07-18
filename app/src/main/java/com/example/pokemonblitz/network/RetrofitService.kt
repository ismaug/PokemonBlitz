package com.example.pokemonblitz.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val expiresIn: Long)

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body request: Map<String, String>): Response<Unit>

}
