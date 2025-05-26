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
    suspend fun insertDimensions(dimensions: DimensionsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeta(meta: MetaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Transaction
    suspend fun insertProductWithRelations(
        product: ProductEntity,
        dimensions: DimensionsEntity,
        meta: MetaEntity,
        reviews: List<ReviewEntity>
    ) {
        insertProduct(product)
        insertDimensions(dimensions)
        insertMeta(meta)
        reviews.forEach { insertReview(it) }
    }

    @Transaction
    @Query("SELECT * FROM product")
    fun getProductsWithDetails(): Flow<List<ProductWithDetails>>

    @Query("DELETE FROM product")
    suspend fun deleteAllProducts()
}
