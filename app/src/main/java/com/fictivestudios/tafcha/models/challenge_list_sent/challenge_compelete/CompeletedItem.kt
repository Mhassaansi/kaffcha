package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_compelete

data class CompeletedItem(
    var challenge_id: Int?=0,
    var description: String?=null,
    var oponent_data: OponentData,
    var score: String?=null
)