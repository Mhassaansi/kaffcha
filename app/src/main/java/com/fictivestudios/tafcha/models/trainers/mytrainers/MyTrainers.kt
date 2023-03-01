package com.fictivestudios.tafcha.models.trainers.mytrainers

data class MyTrainers(
    var `data`: List<MyTrainerItem>,
    var message: String?=null,
    var status: Int?=0
)