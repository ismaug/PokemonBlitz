package com.example.pokemonblitz.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val expiresIn: Long)
data class ScoreResponse(
    val score: Int,
    val time: Int,
    val correctAnswers: Int
)

data class GlobalScoreResponse(
    val username: String,
    val score: Int,
    val time: Int,
    val correctAnswers: Int,
    val createdAt: String
)
data class UserResponse(val id: String, val username: String, val email: String)
interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body request: Map<String, String>): Response<Unit>

    @GET("/me")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<UserResponse>

    @GET("scores/user")
    suspend fun getUserScores(@Header("Authorization") token: String): List<ScoreResponse>

    @GET("scores/top5")
    suspend fun getTopScores(): List<GlobalScoreResponse>
}
