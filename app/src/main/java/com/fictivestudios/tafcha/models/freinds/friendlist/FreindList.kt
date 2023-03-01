package com.fictivestudios.tafcha.models.freinds.friendlist

data class FreindList(
    var `data`: ArrayList<FriendsData>,
    var message: String?=null,
    var status: Int?=0
)