package com.example.moviehub.screens.database.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel :ViewModel(){

    private val auth:FirebaseAuth=FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkStatus()
    }
    fun checkStatus(){
        if(auth.currentUser!=null){
            _authState.value=AuthState.Authenticated
        }else{
            _authState.value=AuthState.UnAuthenticated
        }
    }

    fun loginWithCredentals(email:String,password:String){
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            _authState.value = AuthState.ErrorMessage("Please enter both email and password.")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                task->
                if(task.isSuccessful){
                    _authState.value=AuthState.Authenticated
                }else{
                    _authState.postValue(
                        AuthState.ErrorMessage(
                            task.exception?.message ?: "An unknown error occurred."
                        ))
                }
            }
    }

    fun signUpUser(email:String,password: String){
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            _authState.value = AuthState.ErrorMessage("Please enter both email and password.")
            return
        }
        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                    task->
                if(task.isSuccessful){
                    _authState.value=AuthState.Authenticated
                }else{
                    _authState.postValue(
                        AuthState.ErrorMessage(
                            task.exception?.message ?: "An unknown error occurred."
                        ))
                }
            }
    }

    fun logoutUser(){
        auth.signOut()
        checkStatus()

    }
}

sealed class AuthState{
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class ErrorMessage(val message: String) : AuthState()
}