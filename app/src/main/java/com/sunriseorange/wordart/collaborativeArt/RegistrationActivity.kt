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

// Registers A NEW user if they do not exist
// and makes sure that the password fits the
// requirements (using validators class) and
// then stores it all in firebase so that they
// can log into the app later with the email
// and pssword that they have registered with
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

        // Once everthing is entered go into the registerNewUser function
        registerButton.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {
        loadingBar.visibility = View.VISIBLE

        val email: String = userEmail.text.toString()
        val password: String = userPassword.text.toString()

        // Using validator, if the email is not valid, give
        // error message saying "Please enter valid email"
        if (!validator.validEmail(email)) {
            Toast.makeText(
                applicationContext,
                "Please enter valid email",
                Toast.LENGTH_LONG
            ).show()
            loadingBar.visibility = View.GONE
            userEmail.text.clear()
            return
        }

        // Using validator, if the password is not valid, give
        // error message listing requirements for a valid password
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

        // register the user as email and password are valid
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
                // go to the dashboard as it was successful
                startActivity(
                    Intent(
                        this@RegistrationActivity,
                        DashboardActivity::class.java
                    )
                )
            }
            // If registration not successful give error registration failed
            else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.register_failed_string),
                    Toast.LENGTH_LONG
                ).show()

                // return to front page
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
