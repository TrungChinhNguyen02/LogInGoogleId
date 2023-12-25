package com.example.logingoogleid.model

import android.util.Log
import com.example.logingoogleid.interfaceModel.ISignUp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class SignUp(private val iSignUp: ISignUp) {
    val TAG ="SignUp"
    private lateinit var auth: FirebaseAuth

    fun CreateAccount(email : String, password:String){
        auth = Firebase.auth
        val TAG ="CreateAccount"

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
            if (task.isSuccessful){
                iSignUp.signUpSuccess()
            }
            else{
                iSignUp.signUpFail()
            }
        }
    }
    fun saveUser( firstName : String, lastName : String, email : String){
        val mUser = User(firstName,lastName,email)
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("User")
        userRef.child(auth.uid.toString()).setValue(mUser)
        Log.d(TAG, "saveUser: ahiahi ")
    }
}

