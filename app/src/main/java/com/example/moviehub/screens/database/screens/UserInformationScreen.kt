package com.example.moviehub.screens.database.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moviehub.navigation.Screen
import com.example.moviehub.screens.database.authentication.UserDatabase

@Composable
fun UserInformationScreen(navHostController: NavHostController){
    val firstName= remember { mutableStateOf("") }
    val lastName= remember { mutableStateOf("") }
    val userDatabase=UserDatabase(LocalContext.current)
Column(modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = "Enter Your First Name")
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = firstName.value,
        onValueChange = {
            firstName.value=it
        }
    )
    Spacer(modifier = Modifier.height(15.dp))
    Text(text = "Enter Your Last Name")
    Spacer(modifier = Modifier.height(5.dp))
    OutlinedTextField(
        value = lastName.value,
        onValueChange = {
            lastName.value=it
        }
    )
    Spacer(modifier = Modifier.height(15.dp))
    Button(onClick = {
    userDatabase.createUser(firstName.value,lastName.value)
        navHostController.navigate(Screen.Home.route)
    }) {

        Text("Submit")
    }


}
}
