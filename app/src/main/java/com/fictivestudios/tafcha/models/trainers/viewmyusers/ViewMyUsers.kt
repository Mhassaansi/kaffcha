package com.fictivestudios.tafcha.models.trainers.viewmyusers

data class ViewMyUsers(
    var `data`: List<MyUserItem>,
    var message: String,
    var status: Int
)