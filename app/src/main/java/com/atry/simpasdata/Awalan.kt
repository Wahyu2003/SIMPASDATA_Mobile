package com.atry.simpasdata

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Awalan : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awalan)

        // Assuming you have a button with the id "nextButton" in your activity_awalan.xml
        val nextButton: Button = findViewById(R.id.button)

        nextButton.setOnClickListener {
            // Create an Intent to start the next activity
            val intent = Intent(this, LoginActivity::class.java)

            // You can also pass data to the next activity using intent.putExtra if needed
            // intent.putExtra("key", "value")

            // Start the next activity
            startActivity(intent)
        }
    }
}
