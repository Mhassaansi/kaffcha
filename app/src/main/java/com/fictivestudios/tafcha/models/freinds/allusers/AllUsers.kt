package com.fictivestudios.tafcha.models.freinds.allusers

data class AllUsers(
    var data: List<FriendAllUserItem>,
    var message: String?=null,
    var status: Int?=0
)