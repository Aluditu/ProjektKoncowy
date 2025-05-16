package com.example.projektkoncowy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektkoncowy.viewmodel.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    if (viewModel.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (viewModel.errorMessage != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Błąd: ${viewModel.errorMessage}")
        }
    } else {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Posty",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Divider()
            LazyColumn {
                itemsIndexed(viewModel.postWithUsers) { index, item ->
                    val backgroundColor = if (index % 2 == 0) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .background(backgroundColor)
                            .clickable { navController.navigate("postDetail/${item.post.id}") }
                            .padding(8.dp)
                    ) {
                        Text(item.post.title, fontWeight = FontWeight.Bold)
                        Text("Autor: ${item.user.name}", modifier = Modifier.clickable {
                            navController.navigate("userDetail/${item.user.id}")
                        })
                    }
                    Divider()
                }
            }
        }
    }
}
