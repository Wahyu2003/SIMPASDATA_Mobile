package com.atry.simpasdata.network

import com.atry.simpasdata.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Field
import  retrofit2.http.FormUrlEncoded
import  retrofit2.http.POST

interface ApiClient{
    @FormUrlEncoded
    @POST("SIMPASDATA_WEB/database/login.php")
    fun login (
        @Field("post_nipnisn") nip : String,
        @Field("post_password") password : String
        ): Call<ResponseLogin>
}