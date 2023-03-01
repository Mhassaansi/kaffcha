package com.fictivestudios.tafcha.models.feed

data class VoterItem(
    var challenge_giver_id: Int?=0,
    var challenge_id: Int?=0,
    var challenge_reciever_id: Int?=0,
    var created_at: String?=null,
    var id: Int?=0,
    var updated_at: String?=null,
    var vote_from_id: Int?=0
)