package com.segunfrancis.sparkshop.utils

import java.util.Locale

fun String?.toTitleCase(locale: Locale = Locale.getDefault()): String {
    if (this.isNullOrEmpty()) return this ?: ""

    return this
        .trim()
        .split("\\s+".toRegex())
        .joinToString(" ") { word ->
            when {
                word.isEmpty() -> ""
                word.length == 1 -> word.uppercase(locale)
                else -> word.substring(0, 1).uppercase(locale) +
                        word.substring(1).lowercase(locale)
            }
        }
        .replace(Regex("\\s+"), " ")
}
