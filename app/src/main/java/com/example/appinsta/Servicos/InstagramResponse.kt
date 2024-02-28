package com.example.appinsta.Servicos

import com.google.gson.annotations.SerializedName

data class InstagramResponse(
    @SerializedName("order")
    val order: Int // ou outro tipo de dado, dependendo do que é retornado pela API
)
