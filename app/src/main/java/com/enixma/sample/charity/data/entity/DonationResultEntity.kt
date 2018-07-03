package com.enixma.sample.charity.data.entity

import com.google.gson.annotations.SerializedName

class DonationResultEntity() {
    var statusCode: Int = 201
    @SerializedName("success")
    var isSuccess: Boolean = false
    @SerializedName("error_code")
    var errorCode: String = ""
    @SerializedName("error_message")
    var errorMessage: String = ""
}