package com.elmaddinasger.ecommerce.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppRetrofit {
    private const val BASE_URL = "https://fakestoreapi.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productServices: ProductServices  = retrofit.create(ProductServices::class.java)
}