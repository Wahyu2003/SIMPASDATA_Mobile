package com.atry.simpasdata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.cardview.widget.CardView
import RetrofitClient
import android.content.SharedPreferences

import com.atry.simpasdata.model.Response_Profile
import com.atry.simpasdata.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.TextView

class DashboardActivity : AppCompatActivity() {

    private lateinit var profileNameTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize the view
        profileNameTextView = findViewById(R.id.profile_name)
        // Get NISN from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val nipnisn: String? = sharedPreferences.getString("NISN", "")
        Log.d("NISN", "NISN: $nipnisn")
        // Check if NISN is not empty before making API call
        if (nipnisn?.isNotEmpty() == true) {
            // Make Retrofit call to get profile based on NISN
            val api = RetrofitClient().getInstance()
            val call: Call<Response_Profile> = api.getName(nipnisn)

            call.enqueue(object : Callback<Response_Profile> {
                override fun onResponse(
                    call: Call<Response_Profile>,
                    response: Response<Response_Profile>
                ) {
                    if (response.isSuccessful) {
                        val profileResponse: Response_Profile? = response.body()
                        // Check if the profileResponse is not null and the list of profiles is not empty
                        if (profileResponse != null && profileResponse.nama.isNotEmpty()) {
                            // Update UI with the obtained name
                            profileNameTextView.text = profileResponse.nama[0].nama
                        } else {
                            // Handle the case where the profileResponse is null or the list of profiles is empty
                            Toast.makeText(
                                this@DashboardActivity,
                                "Data nama tidak valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle unsuccessful response
                        Log.e("Response", "Unsuccessful response: ${response.message()}")
                        Toast.makeText(
                            this@DashboardActivity,
                            "Gagal mendapatkan nama",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Response_Profile>, t: Throwable) {
                    // Handle failure (e.g., network issues)
                    Log.e("DashboardActivity", "Gagal terhubung ke server: ${t.message}")
                    Toast.makeText(
                        this@DashboardActivity,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            // Handle the case where NISN is empty
            Toast.makeText(this, "NISN kosong", Toast.LENGTH_SHORT).show()
        }
        // ... (Other code inside onCreate)


        val home: CardView = findViewById(R.id.Home)
        val akun: CardView = findViewById(R.id.Akun)
        val kalender: CalendarView = findViewById(R.id.kalender)

        kalender.setOnDateChangeListener { kalender, i, i2, i3 ->
            Toast.makeText(this@DashboardActivity, "Selected Date:$i3/$i2$i", Toast.LENGTH_LONG)
                .show()
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


