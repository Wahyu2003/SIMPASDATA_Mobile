package com.atry.simpasdata

import RetrofitClient
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.model.Response_Profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ProfileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val edit: Button = findViewById(R.id.edit_profile)
        val back : Button = findViewById(R.id.kembali)

        back.setOnClickListener(View.OnClickListener {
            // Start the next activity when the CardView is clicked
            val intent = Intent(this@ProfileActivity, DashboardActivity::class.java)
            startActivity(intent)
        })


        edit.setOnClickListener {
            val intent = Intent(this, edit_profil::class.java)
            startActivity(intent)
        }

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
                            val kelasTextView: TextView = findViewById(R.id.kelas)
                            val fotoImageView: ImageView = findViewById(R.id.imageView3)


                            nisnTextView.text = "NISN: ${profile.nisn}"
                            namaTextView.text = "Nama: ${profile.nama}"
                            emailTextView.text = "Email: ${profile.email}"
                            noHpTextView.text = "No.HP: ${profile.no_hp}"
                            alamatTextView.text = "Alamat: ${profile.alamat}"
                            roleTextView.text = "Role: ${profile.role}"
                            kelasTextView.text = "Kelas: ${profile.kelas}"



                            loadProfileImage(profile.foto, fotoImageView)

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
    fun loadProfileImage(imageBase64: String?, imageView: ImageView) {
        if (!imageBase64.isNullOrEmpty()) {
            // Menggunakan Coroutines untuk melakukan operasi jaringan di background
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val imageBytes: ByteArray = Base64.decode(imageBase64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    // Menggunakan Dispatcher Main untuk memperbarui UI di thread utama
                    launch(Dispatchers.Main) {
                        imageView.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        }

}


