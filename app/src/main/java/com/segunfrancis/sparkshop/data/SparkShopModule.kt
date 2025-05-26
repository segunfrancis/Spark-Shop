package com.segunfrancis.sparkshop.data

import android.content.Context
import com.segunfrancis.sparkshop.data.local.CartDao
import com.segunfrancis.sparkshop.data.local.Converters
import com.segunfrancis.sparkshop.data.local.ProductDao
import com.segunfrancis.sparkshop.data.local.SparkShopDatabase
import com.segunfrancis.sparkshop.data.remote.SparkShopApi
import com.segunfrancis.sparkshop.utils.BASE_URL_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SparkShopModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(json: Json, client: OkHttpClient): SparkShopApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(SparkShopApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideConverters(json: Json): Converters {
        return Converters(json)
    }

    @Provides
    @Singleton
    fun provideProductDao(
        @ApplicationContext context: Context,
        converters: Converters
    ): ProductDao {
        return SparkShopDatabase.getDatabase(context, converters).productDao()
    }

    @Provides
    @Singleton
    fun provideCartDao(
        @ApplicationContext context: Context,
        converters: Converters
    ): CartDao {
        return SparkShopDatabase.getDatabase(context, converters).cartDao()
    }
}
