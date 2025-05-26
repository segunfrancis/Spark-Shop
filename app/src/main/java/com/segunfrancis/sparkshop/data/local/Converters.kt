package com.segunfrancis.sparkshop.data.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(private val json: Json) {


    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        return json.decodeFromString(ListSerializer(String.serializer()), value)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        return json.encodeToString(ListSerializer(String.serializer()), list ?: emptyList())
    }

    @TypeConverter
    fun fromReviewsList(reviews: List<ReviewEntity?>): String {
        return json.encodeToString(reviews)
    }

    @TypeConverter
    fun toReviewsList(reviewsString: String): List<ReviewEntity?> {
        return try {
            json.decodeFromString(reviewsString)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
