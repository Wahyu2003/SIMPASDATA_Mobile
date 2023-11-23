package com.atry.simpasdata

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class edit_profil : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var filePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        val tambah: Button = findViewById(R.id.tambah)
        val save: Button = findViewById(R.id.save)

        tambah.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        save.setOnClickListener {
            filePath?.let { uploadImage(it) } ?: run {
                Toast.makeText(this@edit_profil, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI gambar dari hasil pemilihan
            val selectedImageUri: Uri = data.data!!

            // Dapatkan path file dari URI
            filePath = getRealPathFromURI(selectedImageUri)

            // Ambil NISN dari SharedPreferences
            val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val nisn = sharedPref.getString("nisn", "")

            // Sekarang 'filePath' berisi path file sebenarnya dari gambar yang dipilih
            Log.d("FilePath", filePath ?: "Path is null")

            // Periksa apakah ada kode yang memindahkan pengguna ke kelas "profile"
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("nisn", nisn)
            startActivity(intent)
        }
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var filePath: String = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            it.moveToFirst()
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            filePath = it.getString(columnIndex)
        }

        return filePath
    }

    private fun uploadImage(filePath: String) {
        // Membuat objek File dari path file
        val file = File(filePath)

        // Membuat objek RequestBody untuk file
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

        // Membuat objek MultipartBody.Part dari file
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Mengirim permintaan upload ke server
        ApiClient.instance.uploadImage(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@edit_profil, "Upload successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@edit_profil, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Upload Error", t.message ?: "Unknown error")
                Toast.makeText(this@edit_profil, "Upload failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
