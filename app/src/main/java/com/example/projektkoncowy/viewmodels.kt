package com.example.projektkoncowy.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projektkoncowy.model.*
import com.example.projektkoncowy.repository.MainRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository = MainRepository()) : ViewModel() {
    var postWithUsers by mutableStateOf<List<PostWithUser>>(emptyList())
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        viewModelScope.launch {
            try {
                val posts = repository.getPosts()
                val users = repository.getUsers()
                postWithUsers = posts.mapNotNull { post ->
                    val user = users.find { it.id == post.userId }
                    user?.let { PostWithUser(post, it) }
                }
            } catch (e: Exception) {
                errorMessage = e.message
            }
            isLoading = false
        }
    }
}

class PostDetailViewModel(private val repository: MainRepository = MainRepository()) : ViewModel() {
    var post by mutableStateOf<Post?>(null)
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadPost(id: Int) {
        viewModelScope.launch {
            try {
                post = repository.getPost(id)
            } catch (e: Exception) {
                errorMessage = e.message
            }
            isLoading = false
        }
    }
}

class UserDetailViewModel(private val repository: MainRepository = MainRepository()) : ViewModel() {
    var user by mutableStateOf<User?>(null)
    var todos by mutableStateOf<List<Todo>>(emptyList())
    var isLoading by mutableStateOf(true)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadUserAndTodos(userId: Int) {
        viewModelScope.launch {
            try {
                val userDeferred = async { repository.getUser(userId) }
                val todosDeferred = async { repository.getTodos(userId) }
                user = userDeferred.await()
                todos = todosDeferred.await()
            } catch (e: Exception) {
                errorMessage = e.message
            }
            isLoading = false
        }
    }
}