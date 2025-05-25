package com.segunfrancis.sparkshop.data.repository

import com.segunfrancis.sparkshop.data.remote.Product

interface SparkShopRepository {

    suspend fun getAllProducts(): Result<List<Product>>
}
