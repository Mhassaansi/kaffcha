package com.fictivestudios.tafcha.models.reminder.ReminderResponse

data class ReminderResponse(
    var reminderItem: ReminderItem,
    var message: String,
    var status: Int
)