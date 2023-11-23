package com.atry.simpasdata.network

import com.atry.simpasdata.model.ResponseLogin
import com.atry.simpasdata.model.Response_Profile
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiClient {

    @FormUrlEncoded
    @POST("SIMPASDATA_WEB/database/login.php")
    fun login(
        @Field("post_nipnisn") nip: String,
        @Field("post_password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("SIMPASDATA_WEB/database/dash.php")  // Sesuaikan dengan alamat API di server
    fun getName(
        @Field("nipnisn") nipnisn: String
    ): Call<Response_Profile>

    @Multipart
    @POST("SIMPASDATA_WEB/database/update.php") // Ganti dengan nama skrip PHP di server Anda
    fun uploadImage(@Part file: MultipartBody.Part): Call<ResponseBody>

    companion object {
        private const val BASE_URL = "your_base_url_here"  // Replace with your API base URL

        val instance: ApiClient by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiClient::class.java)
        }
    }
}
