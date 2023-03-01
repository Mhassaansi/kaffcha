package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_recived

data class Reciever(
    var bio: String?=null,
    var email: String?=null,
    var id: Int?=0,
    var image: String?=null,
    var is_social: String?=null,
    var name: String?=null,
    var notification: Int?=0,
    var phone: String?=null
)