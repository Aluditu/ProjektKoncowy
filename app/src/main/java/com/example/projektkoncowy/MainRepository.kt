package com.example.projektkoncowy.repository

import com.example.projektkoncowy.network.RetrofitInstance

class MainRepository {
    private val api = RetrofitInstance.api
    suspend fun getPosts() = api.getPosts()
    suspend fun getUsers() = api.getUsers()
    suspend fun getPost(id: Int) = api.getPost(id)
    suspend fun getUser(id: Int) = api.getUser(id)
    suspend fun getTodos(userId: Int) = api.getTodosByUserId(userId)
}