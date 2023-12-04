package com.atry.simpasdata

import RetrofitClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.databinding.LoginBinding
import com.atry.simpasdata.model.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private var binding: LoginBinding? = null
    private var nisn: String = ""
    private var password: String = ""
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        val lupaPassword : TextView = findViewById(R.id.lupa);

        lupaPassword.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@LoginActivity, LupaPassword::class.java)
            startActivity(intent)
        })

        lupaPassword.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@LoginActivity, LupaPassword::class.java)
            startActivity(intent)
        })

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)

        binding!!.buttonLogin.setOnClickListener {
            nisn = binding!!.editTextUsername.text.toString()
            password = binding!!.editTextPassword.text.toString()

            when {
                nisn.isEmpty() -> {
                    binding!!.editTextUsername.error = "NISN atau NIP tidak boleh kosong"
                }
                password.isEmpty() -> {
                    binding!!.editTextPassword.error = "Password tidak boleh kosong"
                }
                else -> {
                    getData()
                }
            }
        }
    }

    private fun getData() {
        val api = RetrofitClient().getInstance()
        api.login(nisn, password).enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    if (response.body()?.response == true) {
                        // Save NISN to SharedPreferences
                        val editor = sharedPreferences.edit()
                        editor.putString("NISN", nisn)
                        editor.apply()

                        // Log saved NISN
                        Log.d("SharedPreferences", "NISN saved: $nisn")

                        // Move to DashboardActivity after successful login
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish() // Close LoginActivity to prevent going back
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Gagal, periksa kembali NISN dan password",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Gagal, terjadi kesalahan",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e("pesan error", "${t.message}")
            }
        })
    }
}
