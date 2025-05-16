package com.example.projektkoncowy.network

import com.example.projektkoncowy.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface JsonPlaceholderApi {
    @GET("posts") suspend fun getPosts(): List<Post>
    @GET("users") suspend fun getUsers(): List<User>
    @GET("posts/{id}") suspend fun getPost(@Path("id") id: Int): Post
    @GET("users/{id}") suspend fun getUser(@Path("id") id: Int): User
    @GET("todos") suspend fun getTodosByUserId(@Query("userId") userId: Int): List<Todo>
}

object RetrofitInstance {
    val api: JsonPlaceholderApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JsonPlaceholderApi::class.java)
    }
}
