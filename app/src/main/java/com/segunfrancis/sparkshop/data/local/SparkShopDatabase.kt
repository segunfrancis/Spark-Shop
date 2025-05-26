package com.segunfrancis.sparkshop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductEntity::class,
        RatingEntity::class,
        DimensionsEntity::class,
        CartItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SparkShopDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: SparkShopDatabase? = null

        fun getDatabase(context: Context): SparkShopDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SparkShopDatabase::class.java,
                    "spark_shop_ecommerce_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
