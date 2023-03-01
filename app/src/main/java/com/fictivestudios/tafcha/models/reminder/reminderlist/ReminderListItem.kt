package com.fictivestudios.tafcha.models.reminder.reminderlist

data class ReminderListItem(
    var description: String? = null,
    var id: Int? = 0,
    var reminder_date: String? = null,
    var user_id: Int? = 0,
    var created_at: String? = null,
    var updated_at: String? = null
)