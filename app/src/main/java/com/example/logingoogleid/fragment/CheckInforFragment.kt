package com.example.logingoogleid.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.example.logingoogleid.R
import com.example.logingoogleid.databinding.FragmentCheckInforBinding
import com.example.logingoogleid.model.SignIn
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.facebook.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.net.URL


class CheckInforFragment : Fragment() {
    val TAG = "CheckInforFragment"
    private lateinit var binding: FragmentCheckInforBinding
    val database = FirebaseDatabase.getInstance()
    val usersRef: DatabaseReference = database.getReference("User")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentCheckInforBinding.inflate(inflater,container,false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInfor()
        getDataRealTime()
        getDataFB()





        binding.save.setOnClickListener {
            putData()
        }

    }
   private fun setInfor(){


        val last  = arguments?.getString("last")
        val first = arguments?.getString("first")
        val email = arguments?.getString("email")
        val img = arguments?.getString("img")

        binding.firstName.setText(first)
        binding.lastName.setText(last)
        binding.mail.setText(email)
        Glide.with(this).load(img).into(binding.imgEmail)
    }

    private fun putData(){
        val last  = binding.lastName.text.toString()
        val first = binding.firstName.text.toString()
        val img = arguments?.getString("img")
        val phone = arguments?.getString("phone")

        val bundle = Bundle()
        val myProfileFragment = MyFrofileFragment()
        bundle.putString("last",last)
        bundle.putString("first",first)
        bundle.putString("img",img.toString())
        bundle.putString("phone",phone.toString())

        myProfileFragment.arguments = bundle

        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.addFragment, myProfileFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun getDataRealTime(){
        if(SignIn.id != null){
            usersRef.child(SignIn.id!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Kiểm tra xem dữ liệu có tồn tại không
                    if (dataSnapshot.exists()) {
                        // Lấy dữ liệu từ dataSnapshot

                        val email = dataSnapshot.child("email").getValue(String::class.java)
                        val firstname = dataSnapshot.child("firstName").getValue(String::class.java)
                        val lastname = dataSnapshot.child("lastName").getValue(String::class.java)
                        Log.d(TAG, "onDataChange: $lastname")
                        binding.mail.setText(email)
                        binding.firstName.setText(firstname)
                        binding.lastName.setText(lastname)

                    } else {
                        Log.d(TAG, "onDataChange: faild")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Xử lý lỗi nếu có
                    println("Error: ${databaseError.message}")
                }
            })
        }
    }
    private fun getDataFB(){
        val parameters = Bundle()
        parameters.putString("fields", "id,name,picture.type(large)")




        // Kiểm tra xem người dùng đã đăng nhập vào ứng dụng Facebook chưa
     /*   if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            // Lấy ID của người dùng
            val userId = Profile.getCurrentProfile()!!.id

        } else {
            // Người dùng chưa đăng nhập, hoặc không có quyền truy cập
            Log.d(TAG, "User not logged in or no access token available.")
        }
*/

// Make the Graph API request
        val graphRequest = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { jsonObject, response -> // Handle the response
            if (jsonObject != null) {
                val userName = jsonObject.getString("name")
                val url = jsonObject.getString("url")
                Log.d("huhu", "getDataFB: ${url}")
                binding.firstName.setText(userName)
                val pictureObject = jsonObject.getJSONObject("picture")
                Log.d("huhu", "getDataFB2: ${pictureObject}")
                val dataObject = pictureObject.getJSONObject("data")
                Log.d("huhu", "getDataFB3: ${dataObject}")
                val imageUrl = dataObject.getString("url")

                Glide.with(requireContext()).load(pictureObject).into(binding.imgEmail)

               /* // Use Glide to load the image and convert it to a Bitmap
                Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            // Handle the Bitmap here, for example, set it to an ImageView
                            binding.imgEmail.setImageBitmap(resource)

                            // Now 'resource' is the Bitmap you can use.
                            Log.d(TAG, "getDataFB: Converted image to Bitmap")
                        }
                    })*/
            }
        }


        graphRequest.parameters = parameters
        graphRequest.executeAsync()

    }
    fun onCompleted(jsonObject: JSONObject?, response: GraphResponse?) {
        if (jsonObject != null) {
            Log.d("FacebookResponse", jsonObject.toString())
        }
    }
}