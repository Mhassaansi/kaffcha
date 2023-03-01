package com.fictivestudios.tafcha.models.profile.profileData

import com.fictivestudios.tafcha.models.profile.profileData.ProfileDataItem

data class ProfileData(
    var `data`: ProfileDataItem,
    var message: String?=null,
    var status: Int?=0
)