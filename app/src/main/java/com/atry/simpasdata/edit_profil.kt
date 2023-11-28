package com.atry.simpasdata

import RetrofitClient
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.model.Response_Profile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class edit_profil : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var tambah: Button
    private lateinit var fotoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        // Inisialisasi elemen UI setelah pemanggilan setContentView
        tambah = findViewById(R.id.tambah_gambar)

        val saveButton: Button = findViewById(R.id.save)

        saveButton.setOnClickListener {
            updateProfile()
        }

        tambah.setOnClickListener {
            pickImage()
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
                override fun onResponse(call: Call<Response_Profile>, response: Response<Response_Profile>) {
                    if (response.isSuccessful) {
                        val profileResponse: Response_Profile? = response.body()

                        if (profileResponse != null && profileResponse.profile != null && profileResponse.profile.isNotEmpty()) {
                            val profile = profileResponse.profile[0]

                            // Update UI with profile data
                            val nisnTextView: TextView = findViewById(R.id.nisn)
                            val namaTextView: TextView = findViewById(R.id.nama)
                            val emailTextView: TextView = findViewById(R.id.email3)
                            val noHpTextView: TextView = findViewById(R.id.nomer)
                            val alamatTextView: TextView = findViewById(R.id.alamat)
                            val roleTextView: TextView = findViewById(R.id.role)
                            val kelasTextView: TextView = findViewById(R.id.kelas)
                            fotoImageView = findViewById(R.id.profile)
                            val fotoprof = ProfileActivity()

                            // Load profile image using Glide
                            fotoprof.loadProfileImage(profile.foto, fotoImageView)
                            nisnTextView.text = "${profile.nisn}"
                            namaTextView.text = "${profile.nama}"
                            emailTextView.text = "${profile.email}"
                            noHpTextView.text = "${profile.no_hp}"
                            alamatTextView.text = "${profile.alamat}"
                            roleTextView.text = "${profile.role}"
                            kelasTextView.text = "${profile.nama_kelas}"
                        } else {
                            // Handle case where profile data is empty
                            Toast.makeText(this@edit_profil, "Data profil tidak valid", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle unsuccessful response
                        Log.e("Response", "Unsuccessful response: ${response.message()}")
                        Toast.makeText(this@edit_profil, "Gagal mendapatkan profil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Response_Profile>, t: Throwable) {
                    // Handle failure (e.g., network issues)
                    Log.e("ProfileActivity", "Gagal terhubung ke server: ${t.message}")
                    Toast.makeText(this@edit_profil, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Handle the case where NISN is empty
            Toast.makeText(this@edit_profil, "NISN kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfile() {
        val ni: TextView = findViewById(R.id.nisn)
        val alam: TextView = findViewById(R.id.alamat)
        val em: TextView = findViewById(R.id.email3)
        val no: TextView = findViewById(R.id.nomer)

        // Mengambil nilai dari elemen-elemen input
        val nisn = ni.text.toString()
        val alamat = alam.text.toString()
        val email = em.text.toString()
        val noHp = no.text.toString()

        // Mengambil Uri gambar dari ImageView
        val imageUri: Uri? = selectedImageUri

        // Konversi Uri gambar menjadi File
        val imageFile = imageUri?.let { uriToFile(it) }

        // Menggunakan RequestBody untuk mengirim data teks dan FileRequestBody untuk mengirim file
        val nisnRequestBody = nisn.toRequestBody("text/plain".toMediaTypeOrNull())
        val alamatRequestBody = alamat.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val noHpRequestBody = noHp.toRequestBody("text/plain".toMediaTypeOrNull())

        // Sebelum melakukan panggilan API, log imageFile dan foto
        Log.d("UpdateProfile", "imageFile: $imageFile")

        val foto: MultipartBody.Part? = if (imageFile != null) {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("foto", imageFile.name, requestFile)
        } else {
            null
        }

        // Sebelum melakukan panggilan API, log foto
        Log.d("UpdateProfile", "foto: $foto")

        val retrofitClient = RetrofitClient()
        val apiService = retrofitClient.getInstance()
        Log.d("UpdateProfile", "nisn: $nisn, alamat: $alamat, email: $email, noHp: $noHp, imageFile: $imageFile")
        val call: Call<ResponseBody> = apiService.updateData(
            nisnRequestBody, alamatRequestBody, emailRequestBody,
            noHpRequestBody, foto
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle respon sukses
                    Toast.makeText(this@edit_profil, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    val intent = Intent (this@edit_profil, ProfileActivity::class.java)
                    startActivity(intent)
                } else {
                    // Handle respon error
                    Toast.makeText(this@edit_profil, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Handle failure (e.g., network issues)
                Log.e("UpdateProfile", "Gagal terhubung ke server", t)
                Toast.makeText(this@edit_profil, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            val foto: ImageView = findViewById(R.id.profile)
            foto.setImageURI(selectedImageUri)
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "temp_image")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        return file
    }
}
