package com.atry.simpasdata

import RetrofitClient
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.atry.simpasdata.model.ProfileItem
import com.atry.simpasdata.model.Response_Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var profileNameTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize the view
        profileNameTextView = findViewById(R.id.profile_name)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)

        // Get NISN from SharedPreferences
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
                    try {
                        if (response.isSuccessful) {
                            val profileResponse: Response_Profile? = response.body()

                            // Check if the profileResponse is not null and the list of profiles is not empty
                            if (profileResponse != null && profileResponse.profile != null) {
                                val profiles: List<ProfileItem> = profileResponse.profile
                                if (profiles.isNotEmpty()) {
                                    val fotoImageView: ImageView = findViewById(R.id.profile)
                                    val fotoprof = ProfileActivity()

                                    val profile: ProfileItem = profiles[0]
                                    val nama: String? = profile.nama
                                    if (nama != null) {
                                        // Update UI with the obtained name
                                        profileNameTextView.text = nama

                                        // Save Role to SharedPreferences
                                        val role: String? = profile.role
                                        if (role != null) {
                                            val editor = sharedPreferences.edit()
                                            editor.putString("role", role)
                                            editor.apply()
                                            // Log saved Role
                                            Log.d("SharedPreferences", "Role saved: $role")
                                        } else {
                                            // Handle the case where 'role' is null
                                            Toast.makeText(
                                                this@DashboardActivity,
                                                "Data role tidak valid (role is null)",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        fotoprof.loadProfileImage(profile.foto, fotoImageView)
                                    } else {
                                        // Handle the case where 'nama' is null
                                        Toast.makeText(
                                            this@DashboardActivity,
                                            "Data nama tidak valid (nama is null)",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    // Handle the case where the list of profiles is empty
                                    Toast.makeText(
                                        this@DashboardActivity,
                                        "List data nama kosong",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // Handle the case where profileResponse or profileResponse.profile is null
                                Toast.makeText(
                                    this@DashboardActivity,
                                    "Data nama tidak valid (response or profile is null)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            // Handle unsuccessful response
                            Log.e("Response", "Unsuccessful response: ${response.message()}")
                            Toast.makeText(
                                this@DashboardActivity,
                                "Gagal mendapatkan nama (unsuccessful response)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("Response", "Exception: ${e.message}")
                        Toast.makeText(
                            this@DashboardActivity,
                            "Terjadi kesalahan saat memproses data",
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
        val logout : Button = findViewById(R.id.logout)
        val kalender: CalendarView = findViewById(R.id.kalender)

        kalender.setOnDateChangeListener { _, i, i2, i3 ->
            Toast.makeText(this@DashboardActivity, "Selected Date:$i3/$i2$i", Toast.LENGTH_LONG)
                .show()
        }

        // Set an OnClickListener for the CardView
        home.setOnClickListener(View.OnClickListener {
            val role = sharedPreferences.getString("role", "")

            // Redirect based on user's role
            when (role) {
                "junior" -> {
                    // User is a junior, redirect to NilaiActivity
                    val intent = Intent(this@DashboardActivity, nilai::class.java)
                    startActivity(intent)
                }
                "senior" -> {
                    // User is a senior, redirect to NilaiSeniorActivity
                    val intent = Intent(this@DashboardActivity, nilai_senior::class.java)
                    startActivity(intent)
                }
                else -> {
                    // Handle the case where role is not recognized or is empty
                    Toast.makeText(
                        this@DashboardActivity,
                        "Role not recognized or is empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        akun.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            startActivity(intent)
        })

        logout.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}
