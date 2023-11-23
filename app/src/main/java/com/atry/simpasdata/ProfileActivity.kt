package com.atry.simpasdata

import RetrofitClient
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.model.Response_Profile

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Get NISN from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val nipnisn: String? = sharedPreferences.getString("NISN", "")
        Log.d("NISN", "NISN: $nipnisn")

        // Check if NISN is not empty before making API call
        if (!nipnisn.isNullOrEmpty()) {

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

                        if (profileResponse != null && profileResponse.profile != null && profileResponse.profile.isNotEmpty()) {

                            val profile = profileResponse.profile[0]

                            // Update UI with profile data
                            val nisnTextView: TextView = findViewById(R.id.nisn)
                            val namaTextView: TextView = findViewById(R.id.name)
                            val emailTextView: TextView = findViewById(R.id.email)
                            val noHpTextView: TextView = findViewById(R.id.nomer)
                            val alamatTextView: TextView = findViewById(R.id.alamat)
                            val roleTextView: TextView = findViewById(R.id.role)

                            nisnTextView.text = "NISN: ${profile.nisn}"
                            namaTextView.text = "Nama: ${profile.nama}"
                            emailTextView.text = "Email: ${profile.email}"
                            noHpTextView.text = "No. HP: ${profile.no_hp}"
                            alamatTextView.text = "Alamat: ${profile.alamat}"
                            roleTextView.text = "Role: ${profile.role}"
                        } else {
                            // Handle case where profile data is empty
                            Toast.makeText(
                                this@ProfileActivity,
                                "Data profil tidak valid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Handle unsuccessful response
                        Log.e("Response", "Unsuccessful response: ${response.message()}")
                        Toast.makeText(
                            this@ProfileActivity,
                            "Gagal mendapatkan profil",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Response_Profile>, t: Throwable) {
                    // Handle failure (e.g., network issues)
                    Log.e("ProfileActivity", "Gagal terhubung ke server: ${t.message}")
                    Toast.makeText(
                        this@ProfileActivity,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            // Handle the case where NISN is empty
            Toast.makeText(this, "NISN kosong", Toast.LENGTH_SHORT).show()
        }
    }
}
