package com.segunfrancis.sparkshop.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        ProductEntity::class,
        DimensionsEntity::class,
        MetaEntity::class,
        ReviewEntity::class,
        CartItemEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SparkShopDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: SparkShopDatabase? = null

        fun getDatabase(context: Context, converters: Converters): SparkShopDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SparkShopDatabase::class.java,
                    "spark_shop_ecommerce_database"
                )
                    .addTypeConverter(converters)
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
