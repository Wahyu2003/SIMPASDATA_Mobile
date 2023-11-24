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
    val nama_kelas: String,
    val foto: String
)


data class UpdateProfileData(
    val alamat: String,
    val email: String,
    val noHp: String
)

data class RequestUpdateProfile(
    val updateProfileData: UpdateProfileData,
    val foto: MultipartBody.Part
)


