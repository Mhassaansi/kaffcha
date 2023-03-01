package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_compelete

data class Compelete(
    var `data`: List<CompeletedItem>,
    var message: String?=null,
    var status: Int?=0
)