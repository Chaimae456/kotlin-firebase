package com.example.mycontactapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.mycontactapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivitySignupBinding
    // ActionBar
    private lateinit var actionBar: ActionBar
    // PregressDialog
    private lateinit var progressDialog: ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure Actionbar, // enable back button

        actionBar = supportActionBar!!
        actionBar.title = "sign up"
        // enable back button
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setMessage("Creating account  ...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        // handle click, begin signup
        binding.signupBtn.setOnClickListener {
            // validate data
            validateData()
        }
    }

    private fun validateData() {
        // GET DATA
        email = binding.emailEd.text.toString().trim()
        password = binding.passwordEd.text.toString().trim()


        // validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            binding.emailEd.error = "Invalid Email format"
        }else if (TextUtils.isEmpty(password)){
        // password isn't entered
            binding.passwordEd.error = "Please enter password"
        }else if (password.length <6){
        // password lenght is less then 6
            binding.passwordEd.error = "password must at least 6 characters long"
        }else{
            // data is validated , continue signup
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        // show progress
        progressDialog.show()
        // create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //signup success
                progressDialog.dismiss()
                // get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account created with email $email" , Toast.LENGTH_SHORT).show()

                // OPEN PROFILE
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener {e->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}" , Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when button at actionbar clicked
        return super.onSupportNavigateUp()
    }
}