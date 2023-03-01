package com.fictivestudios.tafcha.models.profile.userprofile

data class UserMatchsData(
    var die: Int?=0,
    var lose: Int?=0,
    var total_points: Int?=0,
    var user: User?,
    var win: Int?=0
)