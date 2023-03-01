package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent

data class ChallengeSent(
    var `data`: List<ChallengeSentItem>,
    var message: String?=null,
    var status: Int?=0
)