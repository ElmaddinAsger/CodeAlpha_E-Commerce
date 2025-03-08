package com.elmaddinasger.ecommerce.retrofit

import com.elmaddinasger.ecommerce.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductServices {

    @GET("products")
    suspend fun getProduct(
    ): Response<Product>
}
