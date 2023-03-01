package com.fictivestudios.tafcha.models.excercies

data class User(
    var device_token: String,
    var email: String,
    var id: Int,
    var image: String,
    var name: String,
    var password: String
)