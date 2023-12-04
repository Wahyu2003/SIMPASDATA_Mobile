package com.atry.simpasdata.network


import com.atry.simpasdata.model.JuniorData
import com.atry.simpasdata.model.ResponseLogin
import com.atry.simpasdata.model.Response_Profile
import com.atry.simpasdata.model.SeniorData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiClient {

    @FormUrlEncoded
    @POST("SIMPASDATA_Web/database/login.php")
    fun login(
        @Field("post_nipnisn") nip: String,
        @Field("post_password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("SIMPASDATA_Web/database/dash.php")  // Sesuaikan dengan alamat API di server
    fun getName(
        @Field("nipnisn") nipnisn: String
    ): Call<Response_Profile>

    @Multipart
    @POST("SIMPASDATA_Web/database/update.php")
    fun updateData(
        @Part("nisn") nisn: RequestBody,
        @Part("alamat") alamat: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("no_hp") noHp: RequestBody?,
        @Part foto: MultipartBody.Part?,
    ): Call<ResponseBody>


    // JuniorApi.kt
    @FormUrlEncoded
    @POST("SIMPASDATA_Web/database/nilai.php") // Sesuaikan dengan path PHP Anda
    fun getJuniorData(
        @Field("nisn") nisn: String
    ): Call<JuniorData>
    @FormUrlEncoded
    @POST("SIMPASDATA_Web/database/nilai_senior.php") // Sesuaikan dengan path PHP Anda
    fun getSeniorData(
        @Field("nisn") nisn: String
    ): Call<SeniorData>

}







