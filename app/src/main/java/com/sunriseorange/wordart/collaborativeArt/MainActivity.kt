package com.sunriseorange.wordart.collaborativeArt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sunriseorange.wordart.R

// The MainActivity class is what you are greeted with when
// you open the application. In this class we have 2 buttons
// that either allow you to register or login depending on if
// you already have an account. Each button redirects you to
// another screen.
class MainActivity : AppCompatActivity() {

    // private variables of the 2 buttons
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    // Super class of Home screen that opens when logging into the app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialization of both buttons
        registerButton = findViewById(R.id.register)
        loginButton = findViewById(R.id.login)

        // If the register button is clicked direct to
        // registration activity
        registerButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    RegistrationActivity::class.java
                )
            )
        }

        // If the login button is clicked direct to
        // login activity
        loginButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
            )
        }
    }
}
