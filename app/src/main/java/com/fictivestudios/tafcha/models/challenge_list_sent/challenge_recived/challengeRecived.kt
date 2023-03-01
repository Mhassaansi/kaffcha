package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_recived

data class challengeRecived(
    var `data`: List<ChallengeReceivedItem>,
    var message: String?=null,
    var status: Int?=0
)