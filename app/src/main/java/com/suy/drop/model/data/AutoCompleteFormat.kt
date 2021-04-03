package com.suy.drop.model.data

import com.squareup.moshi.Json

data class AutoCompleteFormat(
    @Json(name = "main_text") val mainText: String? = null,
    @Json(name = "secondary_text") val secondaryText: String? = null
)
