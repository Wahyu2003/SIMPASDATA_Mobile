package com.atry.simpasdata
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.model.ResetPasswordResponse
import com.atry.simpasdata.model.VerifyOTPResponse
import com.atry.simpasdata.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Verifikasi : AppCompatActivity() {

    private lateinit var etOTP: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var btnVerifyOTP: Button
    private lateinit var btnResetPassword: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verifikasi)

        etOTP = findViewById(R.id.etOTP)
        etNewPassword = findViewById(R.id.etNewPassword)
        btnVerifyOTP = findViewById(R.id.btnVerifyOTP)
        btnResetPassword = findViewById(R.id.btnResetPassword)

        val email = intent.getStringExtra("email")

        btnVerifyOTP.setOnClickListener {
            val enteredOTP = etOTP.text.toString()

            if (enteredOTP.isNotEmpty()) {
                verifyOTP(email, enteredOTP)
            } else {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        btnResetPassword.setOnClickListener {
            val newPassword = etNewPassword.text.toString()

            if (newPassword.isNotEmpty()) {
                resetPassword(email, newPassword)
            } else {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyOTP(email: String?, otp: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.2/") // Ganti dengan URL backend Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiClient::class.java)
        val call = apiService.verifyOTP(email, otp)

        call.enqueue(object : Callback<VerifyOTPResponse> {
            override fun onResponse(call: Call<VerifyOTPResponse>, response: Response<VerifyOTPResponse>) {
                val verifyOTPResponse = response.body()

                if (verifyOTPResponse != null && verifyOTPResponse.success) {
                    // Verifikasi OTP berhasil, lanjut ke langkah selanjutnya
                    // (misalnya, tampilkan pesan sukses atau lanjut ke halaman berikutnya)
                    Toast.makeText(this@Verifikasi, "OTP verification successful.", Toast.LENGTH_SHORT).show()
                } else {
                    // Verifikasi OTP gagal, tampilkan pesan kesalahan
                    Toast.makeText(this@Verifikasi, "Incorrect OTP. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VerifyOTPResponse>, t: Throwable) {
                // Gagal terhubung ke server, tampilkan pesan kesalahan
                Toast.makeText(this@Verifikasi, "Failed to connect to the server. Please check your internet connection.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun resetPassword(email: String?, newPassword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.2/") // Ganti dengan URL backend Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiClient::class.java)
        val call = apiService.resetPassword(email, newPassword)

        call.enqueue(object : Callback<ResetPasswordResponse> {
            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                val resetPasswordResponse = response.body()

                if (resetPasswordResponse != null && resetPasswordResponse.success) {
                    Toast.makeText(this@Verifikasi, "Password reset successful.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Verifikasi, "Failed to reset password. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                Toast.makeText(this@Verifikasi, "Failed to connect to the server. Please check your internet connection.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    }

