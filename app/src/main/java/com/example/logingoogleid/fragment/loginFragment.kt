package com.example.logingoogleid.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.logingoogleid.LoginAccountFragment
import com.example.logingoogleid.R
import com.example.logingoogleid.SignUpFragment
import com.example.logingoogleid.databinding.FragmentLoginBinding
import com.example.logingoogleid.model.CheckPhoneModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import java.util.Arrays


class loginFragment : Fragment() {
    val TAG = "loginFragment"
    private lateinit var binding: FragmentLoginBinding
    private val RC_SIGN_IN = 1234
    private lateinit var client: GoogleSignInClient
    private lateinit var phoneModel: CheckPhoneModel
    private lateinit var callbackManager: CallbackManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phoneModel = ViewModelProvider(this).get(CheckPhoneModel::class.java)

        callbackManager  = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object: FacebookCallback<LoginResult>{
                override fun onCancel() {
                    TODO("Not yet implemented")
                }

                override fun onError(error: FacebookException) {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(result: LoginResult) {
                    val fragmentManager = requireActivity().supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.addFragment, CheckInforFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }

            })
//        login FaceBook
        binding.loginFB.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"));
        }


        binding.btnLogin.isEnabled = false
        phoneModel.isValidPhoneNumber.observe(requireActivity(), Observer { isValid ->
            binding.btnLogin.isEnabled = isValid
        })

        binding.edtSdt.addTextChangedListener {
            phoneModel.validatePhoneNumber(it.toString())
        }
//      login Google
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(requireActivity(), options)
        // gọi đến dialog chọn tài khoản gg
        binding.btnLogin.setOnClickListener {
            val i = client.signInIntent
            startActivityForResult(i, RC_SIGN_IN)
        }
        binding.btnSignUp.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.addFragment, SignUpFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        binding.btnSignInAcc.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.addFragment,LoginAccountFragment()).addToBackStack(null).commit()
        }

    }

    // khi đăng nhập thành công lấy thông tin người dùng để hiển thị
    private fun handleSigninResult(account: GoogleSignInAccount?){
        if (account != null){

            Log.d(TAG, "handleSigninResult: SignIn Success")

            val youEmail = account.email
            val firstName = account.givenName
            Log.d("huhu", "handleSigninResult: ${firstName}")
            val lastName = account.familyName
            val img = account.photoUrl
            val mPhone = binding.edtSdt.text
            val bundle = Bundle()
            val checkInforFragment = CheckInforFragment()

            bundle.putString("last",lastName)
            bundle.putString("first",firstName)
            bundle.putString("email",youEmail)
            bundle.putString("img",img.toString())
            bundle.putString("phone",mPhone.toString())

            checkInforFragment.arguments = bundle

            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.addFragment, checkInforFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }else{
            Log.d(TAG, "handleSigninResult: SignIn Fail")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: $resultCode")
        if (requestCode == RC_SIGN_IN) {

            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                handleSigninResult(account)
            } catch (e: ApiException) {
                handleSigninResult(null)
            }
        }
    }
}