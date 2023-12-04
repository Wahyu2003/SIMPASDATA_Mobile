package com.atry.simpasdata

import RetrofitClient
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atry.simpasdata.model.JuniorData
import com.atry.simpasdata.model.nilai
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class nilai : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nilai)

        // Mengambil NISN dari Shared Preferences
        val sharedPrefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val nisn = sharedPrefs.getString("NISN", "")

        if (nisn.isNullOrEmpty()) {
            // Handle case where NISN is not available
            Toast.makeText(this, "NISN tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        // Mendapatkan instance dari RetrofitClient
        val api = RetrofitClient().getInstance()

        // Mengirim permintaan ke server untuk mendapatkan data nilai berdasarkan NISN
        val call = api.getJuniorData(nisn)

        call.enqueue(object : Callback<JuniorData> {
            override fun onResponse(call: Call<JuniorData>, response: Response<JuniorData>) {
                if (response.isSuccessful) {
                    val juniorData = response.body()

                    if (juniorData?.data?.isNotEmpty() == true) {
                        // Mengambil nilai pertama
                        val nilai1: nilai = juniorData.data[0]

                        // Konversi nilai yang diterima sebagai String menjadi Double
                        val rataSikap: Double = nilai1.rata_sikap.toDouble()
                        val rataPolaPikir: Double = nilai1.rata_pola_pikir.toDouble()
                        val rataKeaktifan: Double = nilai1.rata_keaktifan.toDouble()
                        val rataPBB: Double = nilai1.rata_pbb.toDouble()
                        val rataKeseluruhan: Double = nilai1.rata_keseluruhan.toDouble()
                        val rataPelanggaran: Double = nilai1.rata_pelanggaran.toDouble()

                        // Menampilkan nilai pada TextView
                        val nisnTextView: TextView = findViewById(R.id.nisnTextView)
                        val namaTextView: TextView = findViewById(R.id.namaTextView)
                        val kelasTextView: TextView = findViewById(R.id.kelasTextView)
                        val sikapTextView: TextView = findViewById(R.id.rataSikapTextView)
                        val polapikirTextView: TextView = findViewById(R.id.rataPolaPikirTextView)
                        val keaktifanTextView: TextView = findViewById(R.id.rataKeaktifanTextView)
                        val pbbTextView: TextView = findViewById(R.id.rataPBBTextView)
                        val keseluruhanTextView: TextView = findViewById(R.id.rataKeseluruhanTextView)
                        val pelanggaranTextView: TextView = findViewById(R.id.rataPelanggaranTextView)
                        val alfabetTextView: TextView = findViewById(R.id.nilaiAlfabetTextView)

                        // Menampilkan nilai pada TextView
                        nisnTextView.text = "NISN    :  ${nilai1.nisn}"
                        namaTextView.text = "NAMA    :  ${nilai1.nama}"
                        kelasTextView.text = "KELAS   :  ${nilai1.kelas}"
                        sikapTextView.text = "${rataSikap}"
                        polapikirTextView.text = "${rataPolaPikir}"
                        keaktifanTextView.text = "${rataKeaktifan}"
                        pbbTextView.text = "${rataPBB}"
                        keseluruhanTextView.text = "${rataKeseluruhan}"
                        pelanggaranTextView.text = "${rataPelanggaran}"
                        alfabetTextView.text = "${nilai1.nilai_alfabet}"

                    } else {
                        Toast.makeText(this@nilai, "Data nilai kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@nilai, "Gagal mengambil data nilai", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JuniorData>, t: Throwable) {
                // Pesan Toast onFailure yang lebih deskriptif
                Toast.makeText(this@nilai, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
