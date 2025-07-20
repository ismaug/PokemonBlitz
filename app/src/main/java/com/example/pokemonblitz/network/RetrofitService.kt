package com.example.pokemonblitz.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val expiresIn: Long)
data class ScoreResponse(
    val username: String,
    val score: Int,
    val time: Int
)


interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body request: Map<String, String>): Response<Unit>

    @GET("/scores/user")
    suspend fun getUserScores(@Header("Authorization") token: String): Response<List<ScoreResponse>>

    @GET("/scores/top5")
    suspend fun getTop5Scores(@Header("Authorization") token: String): Response<List<ScoreResponse>>


}
