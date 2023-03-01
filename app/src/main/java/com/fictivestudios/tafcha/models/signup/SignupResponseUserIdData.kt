package com.fictivestudios.tafcha.models.signup

data class SignupResponseUserIdData(
    var data: SignupResponseItem,
    var message: String?=null,
    var status: Int?=0
)