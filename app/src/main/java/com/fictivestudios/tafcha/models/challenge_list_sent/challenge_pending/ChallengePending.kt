package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_pending

data class ChallengePending(
    var `data`: ArrayList<ChallengePendingItem>,
    var message: String?=null,
    var status: Int?=0
)