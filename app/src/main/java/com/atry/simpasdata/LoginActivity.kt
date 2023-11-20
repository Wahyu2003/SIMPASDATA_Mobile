package com.atry.simpasdata

import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.databinding.LoginBinding
import com.atry.simpasdata.model.ResponseLogin

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
        private var binding : LoginBinding? = null
        private var user : String = ""
        private var pass : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.buttonLogin.setOnClickListener {
            user = binding!!.editTextUsername.text.toString()
            pass = binding!!.editTextPassword.text.toString()

            when {
                user == "" -> {
                    binding!!.editTextUsername.error = "Username tidak boleh kosong"
                }
                pass == "" -> {
                    binding!!.editTextPassword.error = "Password tidak boleh kosong"
                }
                else -> {
                    getData()
                }
            }
        }
    }

    private fun getData(){
        val api = RetrofitClient().getInstance()
        api.login(user,pass).enqueue(object : Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    if (response.body()?.response == true) {
                       val Intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(Intent)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Gagal, periksa kembali nip/nisn dan pasword",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Gagal, terjadi kesalahan",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
               Log.e("pesan error","${t.message}")
            }


        })
    }
}


