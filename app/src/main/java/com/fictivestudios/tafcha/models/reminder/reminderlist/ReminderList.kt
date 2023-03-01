package com.fictivestudios.tafcha.models.reminder.reminderlist

data class ReminderList(
    var `data`: List<ReminderListItem>,
    var message: String?=null,
    var status: Int?=0
)