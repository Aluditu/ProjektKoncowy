package com.example.projektkoncowy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektkoncowy.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    if (viewModel.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    if (viewModel.errorMessage != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Błąd: ${viewModel.errorMessage}")
        }
        return
    }

    val allPostsWithUsers = viewModel.postWithUsers

    val users = remember(allPostsWithUsers) {
        allPostsWithUsers.map { it.user }.distinctBy { user -> user.id }
    }

    var selectedUserId by remember { mutableStateOf<Int?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val filteredPosts = remember(allPostsWithUsers, selectedUserId) {
        if (selectedUserId == null) {
            allPostsWithUsers
        } else {
            allPostsWithUsers.filter { it.user.id == selectedUserId }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(onClick = { navController.navigate("profile") }) {
                Text(text = "Moje detale")
            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Filtruj posty po autorze:",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box {
                OutlinedButton(
                    onClick = { dropdownExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = users.find { it.id == selectedUserId }?.name
                            ?: "Wszystkie"
                    )
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Wszystkie") },
                        onClick = {
                            selectedUserId = null
                            dropdownExpanded = false
                        }
                    )
                    users.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.name) },
                            onClick = {
                                selectedUserId = user.id
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(filteredPosts) { index, item ->
                val backgroundColor = if (index % 2 == 0)
                    MaterialTheme.colorScheme.surface
                else
                    MaterialTheme.colorScheme.surfaceVariant

                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .clickable { navController.navigate("postDetail/${item.post.id}") }
                        .padding(8.dp)
                ) {
                    Text(item.post.title, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Autor: ${item.user.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            navController.navigate("userDetail/${item.user.id}")
                        }
                    )
                }
                Divider()
            }
        }
    }
}