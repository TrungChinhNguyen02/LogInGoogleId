package com.example.logingoogleid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.logingoogleid.databinding.FragmentLoginAccountBinding
import com.example.logingoogleid.fragment.CheckInforFragment
import com.example.logingoogleid.interfaceModel.ILoginPassWord
import com.example.logingoogleid.model.SignIn
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class LoginAccountFragment : Fragment(),ILoginPassWord {

    val TAG = "LoginAccountFragment"
    private lateinit var binding: FragmentLoginAccountBinding
    private lateinit var mSignIn : SignIn
    private val auth: FirebaseAuth
    private var email : String ? = null
    init {
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentLoginAccountBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSignIn = SignIn(this)



        binding.btnSignIn.setOnClickListener {
            email = binding.edtEmail.text.toString()
            val password = binding.edtPass.text.toString()
            mSignIn.LogInEmail(email!!,password)
        }
    }

    override fun LoginFail() {
        Log.d(TAG, "LoginFail:đăng nhập thấy bại")
    }

    override fun LoginSuccessful() {
        Toast.makeText(requireContext(), "đăng nhập thành công", Toast.LENGTH_SHORT).show()

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.addFragment, CheckInforFragment())
            transaction.addToBackStack(null)
            transaction.commit()
    }

}