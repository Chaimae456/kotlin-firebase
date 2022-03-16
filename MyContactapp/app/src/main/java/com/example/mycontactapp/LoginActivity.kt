package com.example.mycontactapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.mycontactapp.databinding.LoginActivityBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    //viewBinding
    private lateinit var binding:LoginActivityBinding
    //actionbar
    private lateinit var actionBar: ActionBar
    //progressdialog
    private lateinit var progressDialog:ProgressDialog
    //firebaseauth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"
        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Logging In ...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle Click, open Signup activity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
        // handle click, begin login
        binding.loginBtn.setOnClickListener {
        //before logging in validate data
        validateData()

        }


    }

    private fun validateData() {
        //get data
        email = binding.emailEd.text.toString().trim()
        password = binding.passwordEd.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            binding.emailEd.error = "Invalid email format"
        }else if(TextUtils.isEmpty(password)){
            // no password entered
            binding.passwordEd.error = "Please enter password"

        }
        else{
            // data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
       // show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email ,password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                // get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT).show()
                // open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()

            }
            .addOnFailureListener { e->
                //login Failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        // if user is already logged in go to profile activity
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            // user is already logged in
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}