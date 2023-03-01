package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_sent

data class ChallengeSentItem(
    var challenge_description: String?=null,
    var created_at: String?=null,
    var id: Int?=0,
    var points: Int?=0,
    var reciever: Reciever,
    var reciever_id: Int?=0,
    var reciever_video: String?=null,
    var sender: Sender,
    var sender_id: Int?=0,
    var sender_video: String?=null,
    var status: String?=null,
    var updated_at: String?=null
)