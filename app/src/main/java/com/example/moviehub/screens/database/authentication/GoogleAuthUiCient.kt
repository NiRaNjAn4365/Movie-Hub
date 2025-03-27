package com.example.moviehub.screens.database.authentication

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.navigation.NavHostController
import com.example.moviehub.R
import com.example.moviehub.navigation.Screen
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class GoogleAuthUiClient(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val navHostController: NavHostController
) {
    private var signInLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    fun setSignInLauncher(launcher: ActivityResultLauncher<IntentSenderRequest>) {
        signInLauncher = launcher
    }

    fun signInWithGoogle() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                signInLauncher?.launch(IntentSenderRequest.Builder(result.pendingIntent).build())
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Google Sign-In Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navHostController.navigate(Screen.Home.route)
                    Toast.makeText(context, "Sign-In Successful!", Toast.LENGTH_SHORT).show()
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if(currentUser!=null){
                        val id=currentUser.uid
                        val email=currentUser.email?:""
                        val userName=currentUser.displayName?:"Unknown"
                        val nameParts = userName.split(" ")
                        val firstName = nameParts.firstOrNull() ?: "Unknown"
                        val lastName = if (nameParts.size > 1) nameParts.subList(1, nameParts.size).joinToString(" ") else "Unknown"

                        val user=User(
                            id = id,
                            first_name =firstName,
                            last_name = lastName,
                            email = email
                        )
                        val databaseReference=FirebaseDatabase.getInstance("https://moviehub-b4bc1-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
                        databaseReference.child(id).get().addOnSuccessListener {
                            snapshot->
                            if(snapshot.exists()){
                                Log.d("User Creation","Old user logged in")
                                navHostController.navigate(Screen.Home.route)
                            }else{
                                databaseReference.child(id).setValue(user)
                                    .addOnCompleteListener {
                                        Log.d("User Creation", "New user data saved successfully")
                                        navHostController.navigate(Screen.Home.route)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Error", "Failed to save user: ${e.localizedMessage}")
                                    }
                            }
                            }
                        }
                    }else {
                    Toast.makeText(context, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }

                }

            }
    }
