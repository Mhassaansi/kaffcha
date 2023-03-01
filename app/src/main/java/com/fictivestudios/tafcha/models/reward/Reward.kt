package com.fictivestudios.tafcha.models.reward

data class Reward(
    var `data`: RewardItem,
    var message: String?=null,
    var status: Int?=0
)