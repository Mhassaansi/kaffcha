package com.fictivestudios.tafcha.models.event.event_item

data class EventItem(
    var `data`: EventItemData,
    var message: String?=null,
    var status: Int?=0
)