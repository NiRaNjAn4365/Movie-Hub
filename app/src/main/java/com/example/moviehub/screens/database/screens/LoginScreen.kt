package com.example.moviehub.screens.database.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviehub.R
import com.example.moviehub.navigation.Screen
import com.example.moviehub.screens.database.authentication.AuthState
import com.example.moviehub.screens.database.authentication.AuthViewModel
import com.example.moviehub.screens.database.authentication.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val emailText= remember { mutableStateOf("") }
    val passwordText= remember { mutableStateOf("") }
    val context= LocalContext.current
    val authState=authViewModel.authState.observeAsState()
    val firebaseAuth = FirebaseAuth.getInstance()
    val oneTapClient = Identity.getSignInClient(context)

    val googleAuthUiClient = remember {
        GoogleAuthUiClient(
            context = context,
            firebaseAuth = firebaseAuth,
            oneTapClient = oneTapClient,
            navHostController = navController
        )
    }

    val signInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                googleAuthUiClient.firebaseAuthWithGoogle(idToken)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Sign-In Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
    googleAuthUiClient.setSignInLauncher(signInLauncher)

    LaunchedEffect(authState.value) {
        authState.value?.let { state ->
            when (state) {
                is AuthState.Authenticated -> {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
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
        Text(text = "Login to Dashboard" , fontSize = MaterialTheme.typography.displaySmall.fontSize)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Fill the below form to login", fontWeight = FontWeight.Thin, fontSize = MaterialTheme.typography.titleMedium.fontSize)
        Spacer(modifier = Modifier.height(10.dp))
       Row (horizontalArrangement = Arrangement.SpaceEvenly){
           OutlinedButton(onClick = {
               googleAuthUiClient.signInWithGoogle()
           }) {
               Icon(
                   painter = painterResource(R.drawable.google),
                   contentDescription = "googleImage",
                   modifier = Modifier.size(20.dp)
               )
               Text(text = "Sign in with Google", modifier = Modifier.padding(start = 6.dp))
           }
       }
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
            navController.navigate(Screen.SignUp.route)
        }) {
         Text("Don't have an account create one?")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
        authViewModel.loginWithCredentals(emailText.value,passwordText.value)
        }) {
            Text(text = "Login")
        }
    }
}


