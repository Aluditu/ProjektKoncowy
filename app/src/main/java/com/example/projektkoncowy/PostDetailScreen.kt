package com.example.projektkoncowy.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.projektkoncowy.viewmodel.PostDetailViewModel

@Composable
fun PostDetailScreen(
    navController: NavController,
    postId: Int,
    viewModel: PostDetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    LaunchedEffect(postId) { viewModel.loadPost(postId) }

    if (viewModel.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (viewModel.errorMessage != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Błąd: ${viewModel.errorMessage}")
        }
    } else {
        viewModel.post?.let {
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
                        text = "Szczegóły",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.weight(1f))
                }
                Divider()
                Spacer(Modifier.height(8.dp))
                Text(it.title, fontWeight = FontWeight.Bold)
                Text(it.body)
                Text("   ")
                Text("Autor ID: ${it.userId}")
            }
        }
    }
}
