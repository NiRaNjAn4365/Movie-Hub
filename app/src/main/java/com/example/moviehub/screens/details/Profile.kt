package com.example.moviehub.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviehub.R
import com.example.moviehub.navigation.Screen
import com.example.moviehub.screens.database.authentication.AuthViewModel
import com.example.moviehub.screens.database.authentication.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun Profile(navHostController: NavHostController, authViewModel: AuthViewModel) {
    val user = remember { mutableStateOf<User?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        retrievDetails { result ->
            user.value = result
            isLoading.value = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "profile_image",
                modifier = Modifier
                    .size(125.dp)
                    .clip(CircleShape)
            )

            Text(
                text = user.value?.first_name ?: "Unknown User",
                modifier = Modifier.padding(start = 15.dp),
                fontSize = MaterialTheme.typography.displaySmall.fontSize
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // User Info Section
        if (isLoading.value) {
            Text(text = "Loading...", fontWeight = FontWeight.Thin)
        } else {
            user.value?.let { userData ->
                Column {
                    createRow("First Name", userData.first_name)
                    createRow("Last Name", userData.last_name)
                    createRow("Email", userData.email)
                    createRow("Id", userData.id)
                }
            } ?: Text(text = "Failed to load user data.", color = MaterialTheme.colorScheme.error)
        }

        // Logout Button
        Button(
            onClick = {
                authViewModel.logoutUser()
                navHostController.navigate(Screen.Login.route)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun createRow(headText: String, content: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp)
    ) {
        Text(
            text = headText,
            fontWeight = FontWeight.Thin,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            modifier = Modifier.weight(3f)
        )
        Text(
            text = content,
            modifier = Modifier.weight(4f),
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

fun retrievDetails(onResult: (User?) -> Unit) {
    val auth = FirebaseAuth.getInstance().currentUser
    val databaseReference = FirebaseDatabase.getInstance("https://moviehub-b4bc1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")

    if (auth != null) {
        databaseReference.child(auth.uid).get().addOnSuccessListener {
            val data = it.getValue(User::class.java)
            onResult(data)
        }.addOnFailureListener {
            onResult(null)
        }
    } else {
        onResult(null)
    }
}