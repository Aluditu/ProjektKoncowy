package com.example.projektkoncowy.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektkoncowy.viewmodel.UserDetailViewModel
import com.example.projektkoncowy.ui.map.GoogleMapView

@Composable
fun UserDetailScreen(
    navController: NavController,
    userId: Int,
    viewModel: UserDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    LaunchedEffect(userId) {
        viewModel.loadUserAndTodos(userId)
    }

    if (viewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (viewModel.errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Błąd: ${viewModel.errorMessage}")
        }
    } else {
        viewModel.user?.let { user ->
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Użytkownik",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                Text("DANE UŻYTKOWNIKA:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Text(user.name, fontWeight = FontWeight.Bold)
                Text(user.username)
                Text(user.email)
                Text(user.phone)
                Text(user.website)
                Text("Firma: ${user.company.name}")
                Text("Adres: ${user.address.street}, ${user.address.city}, ${user.address.zipcode}")
                Spacer(modifier = Modifier.height(16.dp))

                val lat = user.address.geo.lat.toDoubleOrNull() ?: 0.0
                val lng = user.address.geo.lng.toDoubleOrNull() ?: 0.0

                Text("Lokalizacja:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                GoogleMapView(
                    lat = lat,
                    lng = lng,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                val completedCount = viewModel.todos.count { it.completed }
                val totalCount = viewModel.todos.size

                Text("Zadania ($completedCount / $totalCount ukończonych):", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                LazyColumn {
                    items(viewModel.todos.sortedBy { it.completed }) { todo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (todo.completed) Color(0xFFDFF0D8) else Color.Transparent)
                                .padding(4.dp)
                        ) {
                            Checkbox(checked = todo.completed, onCheckedChange = null)
                            Text(todo.title)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
