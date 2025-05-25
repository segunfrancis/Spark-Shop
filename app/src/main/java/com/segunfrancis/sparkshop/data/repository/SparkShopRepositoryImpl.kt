package com.segunfrancis.sparkshop.data.repository

import com.segunfrancis.sparkshop.data.remote.Product
import com.segunfrancis.sparkshop.data.remote.SparkShopApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SparkShopRepositoryImpl @Inject constructor(
    private val api: SparkShopApi,
    private val dispatcher: CoroutineDispatcher
) : SparkShopRepository {
    override suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val response = withContext(dispatcher) { api.getAllProducts() }
            Result.success(response.products)
        } catch (t: Throwable) {
            t.printStackTrace()
            Result.failure(t)
        }
    }
}
