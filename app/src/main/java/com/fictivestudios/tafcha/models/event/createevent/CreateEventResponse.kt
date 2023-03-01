package com.fictivestudios.tafcha.models.event.createevent

data class CreateEventResponse(
    var `data`: CreateEventResponseData,
    var message: String?=null,
    var status: Int?=0
)