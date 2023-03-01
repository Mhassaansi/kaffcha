package com.fictivestudios.tafcha.models.event.createevent

data class CreateEventResponseData(
    var created_at: String?=null,
    var date: String?=null,
    var id: Int?=0,
    var image: String?=null,
    var time: String?=null,
    var title: String?=null,
    var updated_at: String?=null,
    var user_id: Int?=0
)