package com.fictivestudios.tafcha.models.reminder.ReminderResponse

data class ReminderItem(
    var created_at: String,
    var description: String,
    var id: Int,
    var reminder_at: String,
    var updated_at: String,
    var user_id: Int
)