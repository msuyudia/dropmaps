package com.suy.drop.model.data

import com.squareup.moshi.Json

data class AutoCompleteResult(
    @Json(name = "place_id") val placeId: String? = null,
    @Json(name = "structured_formatting") val format: AutoCompleteFormat?
)
