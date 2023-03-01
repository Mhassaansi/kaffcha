package com.fictivestudios.tafcha.models.excercies

data class ExerciesItem(
    var created_at: Any,
    var description: String,
    var id: Int,
    var title: String,
    var updated_at: Any,
    var user: User,
    var user_id: Int
)