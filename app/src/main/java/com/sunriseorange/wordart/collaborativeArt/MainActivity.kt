package com.sunriseorange.wordart.collaborativeArt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.sunriseorange.wordart.R


class MainActivity : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Initialization
        registerButton = findViewById(R.id.register)
        loginButton = findViewById(R.id.login)


        loginButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
            )
        }

        registerButton.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    RegistrationActivity::class.java
                )
            )
        }
    }
}
