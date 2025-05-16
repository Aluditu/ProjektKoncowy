package com.example.projektkoncowy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektkoncowy.viewmodel.UserDetailViewModel

@Composable
fun UserDetailScreen(
    navController: NavController,
    userId: Int,
    viewModel: UserDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    LaunchedEffect(userId) { viewModel.loadUserAndTodos(userId) }

    if (viewModel.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (viewModel.errorMessage != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Błąd: ${viewModel.errorMessage}")
        }
    } else {
        viewModel.user?.let { user ->
            Column(Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Użytkownik",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.weight(1f))
                }
                Divider()
                Spacer(Modifier.height(8.dp))

                Text("DANE UŻYTKOWNIKA:", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))

                Text(user.name, fontWeight = FontWeight.Bold)
                Text(user.username)
                Text(user.email)
                Text(user.phone)
                Text(user.website)
                Text("Firma: ${user.company.name}")
                Text("Adres: ${user.address.street}, ${user.address.city}, ${user.address.zipcode}")
                Spacer(Modifier.height(16.dp))

                Text("Zadania:", fontWeight = FontWeight.Bold)
                LazyColumn {
                    items(viewModel.todos.sortedByDescending { it.completed }) { todo ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = todo.completed, onCheckedChange = null)
                            Text(todo.title)
                        }
                    }
                }
            }
        }
    }
}
