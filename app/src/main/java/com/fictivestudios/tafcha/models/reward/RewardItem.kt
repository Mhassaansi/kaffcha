package com.fictivestudios.tafcha.models.reward

data class RewardItem(
    var lose: Int?=0,
    var tie: Int?=0,
    var total_points: Int?=0,
    var win: Int?=0
)