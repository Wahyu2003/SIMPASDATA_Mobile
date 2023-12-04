package com.atry.simpasdata

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.atry.simpasdata.R
import java.util.HashMap

class reset : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        val editText : EditText = findViewById(R.id.email)
        val send : Button = findViewById(R.id.sendOTP)
        val progressBar : ProgressBar = findViewById(R.id.progress)

        send.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            val url = "http://192.168.1.17/SIMPASDATA_Web/database/reset_password.php"

            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    Log.d("Response", response)
                    progressBar.visibility = View.GONE
                    if (response.equals("success", ignoreCase = true)) {
                        val intent = Intent(this@reset, newPw::class.java)
                        intent.putExtra("email", editText.text.toString())
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, response, Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    progressBar.visibility = View.GONE
                    Log.e("Volley Error", "Error: $error")
                    Toast.makeText(applicationContext, "Terjadi kesalahan. Cek logcat untuk detail.", Toast.LENGTH_LONG).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV["email"] = editText.text.toString()
                    return paramV
                }
            }

            stringRequest.setRetryPolicy(object : RetryPolicy {
                override fun getCurrentTimeout(): Int {
                    return 30000
                }

                override fun getCurrentRetryCount(): Int {
                    return 30000 // Ubah ke nilai yang lebih kecil, misalnya 1
                }

                @Throws(VolleyError::class)
                override fun retry(error: VolleyError) {
                    // Tidak melakukan apa-apa
                }
            })

            queue.add(stringRequest)
        })
    }
}
