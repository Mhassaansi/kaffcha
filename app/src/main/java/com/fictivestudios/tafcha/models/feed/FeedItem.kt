package com.fictivestudios.tafcha.models.feed

data class FeedItem(
    var accepters_count: Int?=0,
    var challenge_description: String?=null,
    var created_at: String?=null,
    var givers_count: Int?=0,
    var id: Int?=0,
    var points: Int?=0,
    var reciever: Reciever,
    var sender: Sender,
    var reciever_id: Int?=0,
    var reciever_video: String?=null,
    var sender_id: Int?=0,
    var sender_video: String?=null,
    var status: String?=null,
    var updated_at: String?=null,
    var voter: VoterItem
)