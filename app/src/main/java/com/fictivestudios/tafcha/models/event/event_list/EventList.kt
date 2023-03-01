package com.fictivestudios.tafcha.models.event.event_list

data class EventList(
    var `data`: ArrayList<EventListItem>,
    var message: String?=null,
    var status: Int?=0
)