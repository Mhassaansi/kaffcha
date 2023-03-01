package com.fictivestudios.tafcha.models.leaderBorad

data class LeaderBorad(
    var `data`: ArrayList<LeaderBoradItem>,
    var message: String?=null,
    var status: Int?=0
)