package com.example.logingoogleid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.logingoogleid.databinding.ActivityMainBinding
import com.example.logingoogleid.fragment.loginFragment


class MainActivity : AppCompatActivity() {
    val  TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.addFragment, loginFragment())
        transaction.commit()

    }

}