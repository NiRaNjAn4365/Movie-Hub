package com.example.moviehub.screens.database.authentication

import android.content.Context
import android.util.Log
import com.example.moviehub.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


data class User
    (
           val id:String="",
            val first_name:String="",
            val last_name:String="",
            val email:String=""
            ){
      /*  fun toJson():Map<String,String>{
            return mapOf(
                "id" to id,
                "first_name" to first_name,
                "last_name" to last_name,
                "email" to email
            )
        }

       */
}
class UserDatabase(context:Context) {
    private val databaseUrl=context.getString(R.string.firebase_link)
    private val database:FirebaseDatabase=FirebaseDatabase.getInstance(databaseUrl)
    private val userReference:DatabaseReference=database.getReference("users")
    private val auth:FirebaseAuth=FirebaseAuth.getInstance()
    fun createUser(first_name:String,last_name: String){
        val currenntUser=auth.currentUser
        if(currenntUser!=null){
            val uid=currenntUser.uid
            val email=currenntUser.email?:""
            val newUser=User(
                id = uid,
                first_name=first_name,
                last_name=last_name,
                email=email
            )

            userReference.child(uid).setValue(newUser)
                .addOnSuccessListener {
                    Log.d("User Created","User Created thhough sign up")
                }.addOnFailureListener {
                    Log.d("User Created ","Failed to create User")
                }
        }else{
            println("No authenticated Users found")
        }

    }

}