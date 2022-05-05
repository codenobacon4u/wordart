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
// login class
class LoginActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var loadingBar: ProgressBar
    private var mAuth: FirebaseAuth? = null
    private var validator = Validators()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Setting up the Authentication Database
        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser != null) {
            startActivity(
                Intent(
                    this@LoginActivity,
                    DashboardActivity::class.java
                )
            )
        }
        // Initialization
        userEmail = findViewById(R.id.email)
        userPassword = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        loadingBar = findViewById(R.id.progressBar)

        loginButton.setOnClickListener { loginUserAccount() }
    }

    private fun loginUserAccount() {
        loadingBar.visibility = View.VISIBLE

        val email: String = userEmail.text.toString()
        val password: String = userPassword.text.toString()

        // Check to make sure that the email and password are filled in
        if (!validator.validEmail(email)) {
            Toast.makeText(
                applicationContext,
                "Please enter valid email...",
                Toast.LENGTH_LONG
            ).show()
            loadingBar.visibility = View.GONE
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(
                applicationContext,
                "Please enter valid password!",
                Toast.LENGTH_LONG
            ).show()
            loadingBar.visibility = View.GONE
            return
        }

        val x = mAuth!!.signInWithEmailAndPassword(email, password)
        x.addOnCompleteListener { task ->
            loadingBar.visibility = View.GONE
            // If login/register is successful then go to dashboard screen
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
