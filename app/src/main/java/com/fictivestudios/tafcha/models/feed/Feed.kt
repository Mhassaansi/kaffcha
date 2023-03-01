package com.fictivestudios.tafcha.models.feed

data class Feed(
    var `data`: List<FeedItem>,
    var message: String?=null,
    var status: Int?=0
)