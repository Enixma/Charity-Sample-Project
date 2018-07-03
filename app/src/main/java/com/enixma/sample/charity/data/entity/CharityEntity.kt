package com.enixma.sample.charity.data.entity

import com.google.gson.annotations.SerializedName

class CharityEntity() {
    var id: Int = 0
    var name: String = ""
    @SerializedName("logo_url")
    var logoUrl: String = ""
}