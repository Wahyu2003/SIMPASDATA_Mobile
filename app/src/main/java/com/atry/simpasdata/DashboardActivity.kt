package com.atry.simpasdata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.cardview.widget.CardView

class DashboardActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val home: CardView = findViewById(R.id.Home)
        val akun: CardView = findViewById(R.id.Akun)
        val kalender : CalendarView = findViewById(R.id.kalender)

        kalender.setOnDateChangeListener { kalender, i, i2, i3 ->
            Toast.makeText(this@DashboardActivity, "Selected Date:$i3/$i2$i",Toast.LENGTH_LONG).show()
        }




        // Set an OnClickListener for the CardView
        home.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@DashboardActivity, HomeActivity::class.java)
            startActivity(intent)
        })
        akun.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            startActivity(intent)
        })

    }
    }
