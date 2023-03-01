package com.fictivestudios.tafcha.models.profile.trainer_profile

data class User(
    var account_verified: Int?=0,
    var age: String?=null,
    var bio: String?=null,
    var bio_video: String?=null,
    var created_at: String?=null,
    var date_of_birth: String?=null,
    var device_token: String?=null,
    var device_type: String?=null,
    var email: String?=null,
    var email_verified_at: String?=null,
    var goal: String?=null,
    var height: String?=null,
    var id: Int?=0,
    var image: String?=null,
    var is_social: String?=null,
    var name: String?=null,
    var notification: Int?=0,
    var otp: Int?=0,
    var phone: String?=null,
    var role: String?=null,
    var updated_at: String?=null,
    var user_social_token: String?=null,
    var user_social_type: String?=null,
    var weight: String?=null
)