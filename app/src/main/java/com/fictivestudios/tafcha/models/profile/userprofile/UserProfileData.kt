package com.fictivestudios.tafcha.models.profile.userprofile

data class UserProfileData(
    var data: UserMatchsData,
    var message: String?=null,
    var status: Int?=0
)