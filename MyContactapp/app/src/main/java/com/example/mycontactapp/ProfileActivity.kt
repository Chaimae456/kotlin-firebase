package com.example.mycontactapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.mycontactapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    // View Binding
    private lateinit var binding: ActivityProfileBinding
    // ActionBar
    private lateinit var actionBar: ActionBar
    // FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure ActioonBar
        actionBar = supportActionBar!!
        actionBar.title = "profile"


        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // handle click logout
        binding.logoutbtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
       //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null; user is logged in , get user info
            val email = firebaseUser.email
            // set to text View
            binding.emailTv.text = email

        }else{
            // user null ; user isn't logged in, go to login activity

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}