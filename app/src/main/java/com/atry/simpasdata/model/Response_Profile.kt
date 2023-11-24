package com.atry.simpasdata.model

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
