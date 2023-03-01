package com.fictivestudios.tafcha.models.challenge_list_sent.challenge_detail

data class ChallengeDeatailItem(
    val challenge_description: String?=null,
    val created_at: String?=null,
    val exercise_id: Int?=0,
    val id: Int?=0,
    val points: Int?=0,
    val reciever_id: Int?=0,
    val reciever_video: String?=null,
    val sender_id: Int?=0,
    val sender_video: String?=null,
    val status: String?=null,
    val updated_at: String?=null
)