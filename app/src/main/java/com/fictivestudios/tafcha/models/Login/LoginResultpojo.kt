package com.fictivestudios.tafcha.models.Login

data class LoginResultpojo(
    var bearer_token: String?=null,
    var `data`: LoginResultItem,
    var message: String?=null,
    var status: Int?=0
)