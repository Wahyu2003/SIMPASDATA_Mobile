package com.atry.simpasdata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView

class DashboardActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val cardView: CardView = findViewById(R.id.card1)

        // Set an OnClickListener for the CardView
        cardView.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            startActivity(intent)
        })
    }
    }
