package com.fictivestudios.tafcha.models.commonresponse
import androidx.annotation.Keep

@Keep
data class CommonResponseDC(
    val status: Int?=0,
    val message: String? = null
){}