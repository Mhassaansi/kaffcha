package com.fictivestudios.tafcha.models.freinds.pending

data class PendingFriends(
    var `data`: List<PendingFriendsData>,
    var message: String?=null,
    var status: Int?=0
)