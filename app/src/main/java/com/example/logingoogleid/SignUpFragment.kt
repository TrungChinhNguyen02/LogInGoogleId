package com.example.logingoogleid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.logingoogleid.databinding.FragmentSignUpBinding
import com.example.logingoogleid.interfaceModel.ISignUp
import com.example.logingoogleid.model.SignUp
import com.example.logingoogleid.model.User
import com.google.firebase.Firebase
//import com.example.logingoogleid.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SignUpFragment : Fragment() ,ISignUp{
    val TAG ="SignUpFragment"
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var mSignUp: SignUp
    private val auth: FirebaseAuth
//    val mList : List<User> = ArrayList()
    init {
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSignUp = SignUp(this)

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val pass = binding.edtPass.text.toString()
            mSignUp.CreateAccount(email,pass)


        }

    }

    override fun signUpSuccess() {
        Toast.makeText(requireContext(), "SignUp Success", Toast.LENGTH_SHORT).show()

        mSignUp.saveUser(binding.edtFirst.text.toString(),binding.edtlast.text.toString(),binding.edtEmail.text.toString() )
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.addFragment, LoginAccountFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun signUpFail() {
        Toast.makeText(requireContext(), "SignUp Fail", Toast.LENGTH_SHORT).show()
    }

}