package com.segunfrancis.sparkshop.data.remote

import retrofit2.http.GET

interface SparkShopApi {

    @GET("products")
    suspend fun getAllProducts(): List<Product>
}
