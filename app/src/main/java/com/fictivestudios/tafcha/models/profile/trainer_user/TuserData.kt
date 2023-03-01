package com.fictivestudios.tafcha.models.profile.trainer_user

data class TuserData(
    var die: Int?=0,
    var lose: Int?=0,
    var total_points: Int?=0,
    var user: User,
    var win: Int?=0
)