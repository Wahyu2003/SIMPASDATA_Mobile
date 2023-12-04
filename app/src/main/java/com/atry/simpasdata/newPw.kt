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
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap

class newPw : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pw)

        val otpEditText: EditText = findViewById(R.id.otp)
        val editText: EditText = findViewById(R.id.email)
        val newpasswordEdit: EditText = findViewById(R.id.newPassword)
        val submit: Button = findViewById(R.id.submit)
        val progressBar: ProgressBar = findViewById(R.id.progress)

        submit.setOnClickListener(View.OnClickListener {
            progressBar.visibility = View.VISIBLE
            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            val url = "http://192.168.1.17/SIMPASDATA_Web/database/new_password.php"

            val stringRequest: StringRequest = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener { response ->
                    progressBar.visibility = View.GONE
                    val cleanedResponse = response.trim()
                    Log.d("Response", cleanedResponse) // Tambahkan log
                    if (cleanedResponse.equals("success", ignoreCase = true)) {
                        Toast.makeText(applicationContext, "New Password Set", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@newPw, LoginActivity::class.java)
                        intent.putExtra("email", editText.text.toString())
                        intent.putExtra("otp", otpEditText.text.toString())
                        intent.putExtra("new_password", newpasswordEdit.text.toString())
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, cleanedResponse, Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    progressBar.visibility = View.GONE
                    error.printStackTrace()
                }) {
                override fun getParams(): Map<String, String> {
                    val paramV: MutableMap<String, String> = HashMap()
                    paramV["email"] = editText.text.toString()
                    paramV["otp"] = otpEditText.text.toString()
                    paramV["new_password"] = newpasswordEdit.text.toString()
                    return paramV
                }
            }


            queue.add(stringRequest)
        })

    }
}
