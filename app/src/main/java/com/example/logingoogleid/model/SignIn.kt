package com.example.logingoogleid.model

import android.util.Log
import com.example.logingoogleid.interfaceModel.ILoginPassWord
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignIn(private val mIlogin : ILoginPassWord) {
    val TAG = "SignIn"
   private lateinit var auth : FirebaseAuth
   companion object{
       var id : String? = null
   }

   fun LogInEmail(email : String, password : String){
            auth = Firebase.auth
           auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                  if (task.isSuccessful){
                          mIlogin.LoginSuccessful()
                      id = auth.currentUser!!.uid.toString()
                      Log.d("huhu", "LogInEmail: $id ")

                  }else{
                          mIlogin.LoginFail()

                  }
       }
   }
}
