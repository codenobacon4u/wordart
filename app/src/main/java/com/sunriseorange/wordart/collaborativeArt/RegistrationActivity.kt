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

// Registers A NEW user if they do not exhists
// and makes sure that the pasword fits the
// requirements and then stores it all in firebase
class RegistrationActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var registerButton: Button
    private lateinit var loadingBar: ProgressBar
    private var validator = Validators()

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initializing the Authentication Database
        mAuth = FirebaseAuth.getInstance()

        // Initialization
        userEmail = findViewById(R.id.email)
        userPassword = findViewById(R.id.password)
        registerButton = findViewById(R.id.register)
        loadingBar = findViewById(R.id.progressBar)

        registerButton.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {
        loadingBar.visibility = View.VISIBLE

        val email: String = userEmail.text.toString()
        val password: String = userPassword.text.toString()

        if (!validator.validEmail(email)) {
            Toast.makeText(
                applicationContext,
                "Please enter valid email...",
                Toast.LENGTH_LONG
            ).show()
            loadingBar.visibility = View.GONE
            userEmail.text.clear()
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(
                applicationContext,
                "Password must contain 8 characters with one letter and one number!",
                Toast.LENGTH_LONG
            ).show()
            loadingBar.visibility = View.GONE
            userPassword.text.clear()
            return
        }

        val x = mAuth!!.createUserWithEmailAndPassword(email, password)
        x.addOnCompleteListener { task ->
            loadingBar.visibility = View.GONE
            // If Registration is successful then go to dashboard, otherwise to back to the front page
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.register_success_string),
                    Toast.LENGTH_LONG
                ).show()
                startActivity(
                    Intent(
                        this@RegistrationActivity,
                        DashboardActivity::class.java
                    )
                )
            }
            else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.register_failed_string),
                    Toast.LENGTH_LONG
                ).show()

                startActivity(
                    Intent(
                        this@RegistrationActivity,
                        MainActivity::class.java
                    )
                )
            }
        }
    }
}
