package com.example.hanyarunrun.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.navigation.NavHostController
import com.example.hanyarunrun.viewmodel.ProfileViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.hanyarunrun.viewmodel.saveImageToInternalStorage
import java.io.File
import com.example.hanyarunrun.ui.theme.Lavender9

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }
    val profile by viewModel.profileStateFlow.collectAsState()

    var studentName by rememberSaveable { mutableStateOf(profile?.username ?: "Nama Pengguna") }
    var studentId by rememberSaveable { mutableStateOf(profile?.uid ?: "123456") }
    var studentEmail by rememberSaveable { mutableStateOf(profile?.email ?: "email@example.com") }

    var profileImageUri by rememberSaveable { mutableStateOf(profile?.photoPath) }

    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val imagePath = saveImageToInternalStorage(context, uri)
            if (imagePath != null) {
                profileImageUri = imagePath
                viewModel.updatePhotoPath(imagePath)
            }
        }
    }

    LaunchedEffect(profile) {
        profile?.let {
            studentName = it.username
            studentId = it.uid
            studentEmail = it.email
            profileImageUri = it.photoPath
        }
    }

    if (profile == null) {
        Text("Loading profile...", modifier = Modifier.padding(16.dp))
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .background(Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = profileImageUri?.let { Uri.fromFile(File(it)) }?.toString() + "?timestamp=" + System.currentTimeMillis(),
                        contentDescription = "Profile Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (isEditing) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = studentId,
                        onValueChange = { studentId = it },
                        label = { Text("Student ID") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = studentEmail,
                        onValueChange = { studentEmail = it },
                        label = { Text("Student Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { pickImageLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Lavender9)
                    ) {
                        Text("Upload Photo", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val currentProfile = profile
                            if (currentProfile != null) {
                                val updatedData = currentProfile.copy(
                                    username = studentName,
                                    uid = studentId,
                                    email = studentEmail,
                                    photoPath = profileImageUri
                                )
                                isEditing = false
                                viewModel.updateData(updatedData)
                            }
                        }, modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save")
                    }
                } else {
                    Text(text = studentName, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ID: $studentId", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = studentEmail, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Edit Profile")
                    }
                }
            }
        }
    }
}
