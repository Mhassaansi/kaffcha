package com.fictivestudios.tafcha.models.trainers.allUsers

data class AllUsersForTrainers(
    var `data`: List<UsersTrainers>,
    var message: String,
    var status: Int
)