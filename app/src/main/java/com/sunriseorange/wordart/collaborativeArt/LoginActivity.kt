package com.sunriseorange.wordart.collaborativeArt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.sunriseorange.wordart.R

// Allows a previously registered used to log in acting
// as a gate to get into the Dashboard Activity that only
// users are allowed to enter in. (uses firebase)
class LoginActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var loadingBar: ProgressBar
    private var mAuth: FirebaseAuth? = null
    private var validator = Validators()

    // Verifies login and password credentials using firebase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Setting up the Authentication Database
        mAuth = FirebaseAuth.getInstance()
        // check if the user is authenticated then proceed to
        // the dashboard activity
        if (mAuth!!.currentUser != null) {
            // go to dashboard activity
            startActivity(
                Intent(
                    this@LoginActivity,
                    DashboardActivity::class.java
                )
            )
        }
        // Initialization
        // Get all user info
        userEmail = findViewById(R.id.email)
        userPassword = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        loadingBar = findViewById(R.id.progressBar)

        // direct to longUserAccount function
        loginButton.setOnClickListener { loginUserAccount() }
    }

    private fun loginUserAccount() {
        loadingBar.visibility = View.VISIBLE

        val email: String = userEmail.text.toString()
        val password: String = userPassword.text.toString()

        // Check to make sure that the email and password are filled in
        // and conform to our standards
        if (!validator.validEmail(email)) {
            Toast.makeText(
                applicationContext,
                "Please enter valid email",
                Toast.LENGTH_LONG
            ).show()
            loadingBar.visibility = View.GONE
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(
                applicationContext,
                "Please enter valid password",
                Toast.LENGTH_LONG
            ).show()
            loadingBar.visibility = View.GONE
            return
        }

        // Authenticate the email and password and see if they exist using
        // firebase
        val x = mAuth!!.signInWithEmailAndPassword(email, password)
        x.addOnCompleteListener { task ->
            loadingBar.visibility = View.GONE
            // If login is successful then go to dashboard screen
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Login successful!",
                    Toast.LENGTH_LONG
                ).show()

                startActivity(
                    Intent(
                        this@LoginActivity,
                        DashboardActivity::class.java
                    )
                )
            }
            // if login failed give error message and restart long screen
            else {
                Toast.makeText(
                    applicationContext,
                    "Login failed! Please try again later",
                    Toast.LENGTH_LONG
                ).show()

                startActivity(
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    )
                )
            }
        }
    }
}
