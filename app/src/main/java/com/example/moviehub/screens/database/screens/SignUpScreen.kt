package com.example.moviehub.screens.database.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviehub.navigation.Screen
import com.example.moviehub.screens.database.authentication.AuthState
import com.example.moviehub.screens.database.authentication.AuthViewModel

@Composable
fun SignUpScreen(navController: NavHostController, authViewModel: AuthViewModel){
    val emailText= remember { mutableStateOf("") }
    val passwordText= remember { mutableStateOf("") }
    val context= LocalContext.current
    val authState=authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        authState.value?.let { state ->
            when (state) {
                is AuthState.Authenticated -> {
                    navController.navigate(Screen.Information.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
                is AuthState.ErrorMessage -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
                else -> Unit
            }
        }

    }

    Column(modifier = Modifier.fillMaxSize().padding(start = 25.dp)
        , verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start) {
        Text(text = "Sign In to Dashboard" , fontSize = MaterialTheme.typography.displaySmall.fontSize)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Fill the below form to sign up", fontWeight = FontWeight.Thin, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Email", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = emailText.value, onValueChange = {emailText.value=it}, label = { Text(text = "Enter email address") })

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Password", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = passwordText.value, onValueChange = {passwordText.value=it}, label = { Text(text = "Enter your password") })
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(onClick = {
            navController.navigate(Screen.Login.route)
        }) {
            Text("Already have an Account?Log in")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
        authViewModel.signUpUser(emailText.value,passwordText.value)
        }) {
            Text(text = "Sign Up")
        }
    }
}