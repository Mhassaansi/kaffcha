package com.fictivestudios.tafcha.networkSetup.callhandler

import com.fictivestudios.tafcha.networkSetup.apiinterface.ApiInterface

interface CallHandler<T>{

suspend fun sendRequest (apiInterFace: ApiInterface):T

fun success (response:T)

fun error (message:String)


}