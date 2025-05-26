package com.segunfrancis.sparkshop.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRating(rating: RatingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDimensions(dimensions: DimensionsEntity)

    @Transaction
    suspend fun insertProductWithRelations(
        product: ProductEntity,
        rating: RatingEntity,
        dimensions: DimensionsEntity
    ) {
        insertProduct(product)
        insertRating(rating)
        insertDimensions(dimensions)
    }

    @Transaction
    @Query("SELECT * FROM product")
    fun getProductsWithDetails(): Flow<List<ProductWithDetails>>

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()
}
