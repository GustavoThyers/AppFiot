package com.example.appinsta.Servicos

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST(".")
    fun adicionarEncomenda(
        @Field("key") apiKey: String,
        @Field("action") action: String,
        @Field("service") serviceId: Int,
        @Field("link") link: String,
        @Field("quantity") quantity: Int,

        ): Call<InstagramResponse>
}