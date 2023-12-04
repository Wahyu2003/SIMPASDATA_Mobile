package com.atry.simpasdata
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.model.SendOTPResponse
import com.atry.simpasdata.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LupaPassword : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnSendOTP: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa_password)

        etEmail = findViewById(R.id.etEmail)
        btnSendOTP = findViewById(R.id.btnSendOTP)
                btnSendOTP.setOnClickListener {
                    val email = etEmail.text.toString()

                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.132.188/") // Ganti dengan URL backend Anda
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiService = retrofit.create(ApiClient::class.java)
                    val call = apiService.sendOTP(email)

                    call.enqueue(object : Callback<SendOTPResponse> {
                        override fun onResponse(
                            call: Call<SendOTPResponse>,
                            response: Response<SendOTPResponse>
                        ) {
                            val sendOTPResponse = response.body()

                            if (sendOTPResponse != null && sendOTPResponse.success) {
                                // Kirim OTP berhasil, pindah ke aktivitas verifikasi
                                val intent = Intent(this@LupaPassword, Verifikasi::class.java)
                                intent.putExtra("email", email)
                                startActivity(intent)

                                // Tambahkan log
                                Log.d("LupaPassword", "Berhasil memulai aktivitas Verifikasi")
                            } else {
                                Toast.makeText(
                                    this@LupaPassword,
                                    "KIRIM OTP GAGAL",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                        override fun onFailure(call: Call<SendOTPResponse>, t: Throwable) {
                            Toast.makeText(
                                this@LupaPassword,
                                "GAGAL TERHUBUNG DENGAN SERVER",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }

