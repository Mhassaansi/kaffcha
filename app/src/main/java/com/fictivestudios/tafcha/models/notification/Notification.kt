package com.fictivestudios.tafcha.models.notification

data class Notification(
    var `data`: ArrayList<NotificationItem>,
    var message: String?=null,
    var status: Int?=0
)