package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_detail

data class ChallengeDeails(
    val `data`: ChallengeDeatailItem,
    val message: String?=null,
    val status: Int?=0
)