package com.example.projektkoncowy.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.projektkoncowy.viewmodel.ProfileViewModel
import java.io.File
import java.util.*


fun saveImageToInternalStorage(context: Context, uri: Uri): String {
    val fileName = UUID.randomUUID().toString() + ".jpg"
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
    return fileName
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var firstNameInput by remember { mutableStateOf(uiState.firstName) }
    var lastNameInput by remember { mutableStateOf(uiState.lastName) }
    var imageFileName by remember { mutableStateOf(uiState.imagePath) }

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedFileName = saveImageToInternalStorage(context, it)
            imageFileName = savedFileName
            viewModel.saveImagePath(savedFileName)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "Moje dane",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Wróć"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = firstNameInput,
                onValueChange = {
                    firstNameInput = it
                    viewModel.saveFirstName(it)
                },
                label = { Text("Imię") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = lastNameInput,
                onValueChange = {
                    lastNameInput = it
                    viewModel.saveLastName(it)
                },
                label = { Text("Nazwisko") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (imageFileName.isNotEmpty()) {
                    val imageFile = File(context.filesDir, imageFileName)
                    if (imageFile.exists()) {
                        Image(
                            painter = rememberAsyncImagePainter(imageFile),
                            contentDescription = "Zdjęcie profilowe",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Text("Brak zdjęcia", color = Color.DarkGray)
                    }
                } else {
                    Text("Wybierz zdjęcie", color = Color.DarkGray)
                }

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            getContent.launch("image/*")
                        }
                        .background(Color.Transparent)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Zapisz ustawienia")
            }
        }
    }
}
