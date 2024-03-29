package com.atry.simpasdata.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class Response_Profile(val profile: List<ProfileItem>)

data class ProfileItem(
    val nisn: String,
    val nama: String,
    val email: String,
    val no_hp: String,
    val alamat: String,
    val role: String,
    val kelas: String,
    val foto: String
)




