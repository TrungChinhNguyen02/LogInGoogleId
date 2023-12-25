package com.example.logingoogleid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.logingoogleid.R
import com.example.logingoogleid.databinding.FragmentMyFrofileBinding

class MyFrofileFragment : Fragment() {
    private lateinit var binding: FragmentMyFrofileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentMyFrofileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInfor()
        binding.back.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.addFragment, CheckInforFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
    private fun setInfor(){
        val last  = arguments?.getString("last")
        val first = arguments?.getString("first")
        val phone = arguments?.getString("phone")
        val img = arguments?.getString("img")

        binding.name.text = first + last
        binding.phone.text = phone
        Glide.with(this).load(img).into(binding.imgMyprofile)
    }
}