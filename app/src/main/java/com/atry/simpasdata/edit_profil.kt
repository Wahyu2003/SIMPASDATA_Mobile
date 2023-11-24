package com.atry.simpasdata

import RetrofitClient
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.atry.simpasdata.model.RequestUpdateProfile
import com.atry.simpasdata.model.Response_Profile
import com.atry.simpasdata.model.UpdateProfileData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class edit_profil : AppCompatActivity() {
    private val gson = Gson()

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var tambah: Button
    private lateinit var fotoImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        // Inisialisasi elemen UI setelah pemanggilan setContentView
        tambah = findViewById(R.id.tambah_gambar)
        fotoImageView = findViewById(R.id.profile)
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

                            // Load profile image using Glide
                            loadProfileImage(profile.foto, fotoImageView)
                            nisnTextView.text = "NISN: ${profile.nisn}"
                            namaTextView.text = "Nama: ${profile.nama}"
                            emailTextView.text = "Email: ${profile.email}"
                            noHpTextView.text = "No.HP: ${profile.no_hp}"
                            alamatTextView.text = "Alamat: ${profile.alamat}"
                            roleTextView.text = "Role: ${profile.role}"
                            kelasTextView.text = "Kelas: ${profile.nama_kelas}"
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
        // Get references to TextViews
        val emailTextView: TextView = findViewById(R.id.email3)
        val noHpTextView: TextView = findViewById(R.id.nomer)
        val alamatTextView: TextView = findViewById(R.id.alamat)

        // Get data to be updated
        val alamatBaru = alamatTextView.text.toString()
        val emailBaru = emailTextView.text.toString()
        val noHpBaru = noHpTextView.text.toString()

        // Check if the user has selected a new image
        val isImageSelected = selectedImageUri != null

        // Prepare the image file to be sent as a multipart part
        val fotoPart: MultipartBody.Part? = if (isImageSelected) {
            // Convert URI to File
            val file = File(getRealPathFromUri(selectedImageUri!!))
            // Check if the file exists before creating MultipartBody.Part
            if (file.exists()) {
                val requestFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("foto", file.name, requestFile)
            } else {
                // Handle the case where the file does not exist
                Toast.makeText(this, "File gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
                null
            }
        } else {
            // If no new image selected, set fotoPart to null
            null
        }

        // Create a RequestUpdateProfile object with the updated data, excluding fotoPart
        val updateProfileData = UpdateProfileData(alamatBaru, emailBaru, noHpBaru)

        // Convert updateProfileData to RequestBody
        val gson = Gson()
        val json = gson.toJson(updateProfileData)
        val updateProfileDataRequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        // Make a Retrofit call to send the update data to the server
        val api = RetrofitClient().getInstance()
        val call: Call<Response_Profile> = api.updateProfile(updateProfileDataRequestBody, fotoPart)

        call.enqueue(object : Callback<Response_Profile> {
            override fun onResponse(call: Call<Response_Profile>, response: Response<Response_Profile>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    val responseBody = response.body()
                    if (responseBody != null) {
                        // Handle response body
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(this@edit_profil, "Gagal mengupdate profil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Response_Profile>, t: Throwable) {
                // Handle network errors or other failures
                Toast.makeText(this@edit_profil, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }





    private fun loadProfileImage(imageUrl: String, imageView: ImageView) {
        // Use Glide or other image loading methods here
        Glide.with(this).load(imageUrl).into(imageView)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun convertImageToBase64(uri: Uri): String? {
        val inputStream = contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        return bytes?.let { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            val selectedImageUri: Uri? = data.data
            val fotoBase64: String? = selectedImageUri?.let { convertImageToBase64(it) }

            // Use Glide to load the image into ImageView
            Glide.with(this).load(selectedImageUri).into(fotoImageView)

            // Save the selected image URI for use during profile update
            this.selectedImageUri = selectedImageUri
        }
    }

    private fun getRealPathFromUri(uri: Uri?): String? {
        uri ?: return null // If uri is null, return null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(this, uri, projection, null, null, null)
        val cursor: Cursor? = loader.loadInBackground()

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }

        return null
    }
}
