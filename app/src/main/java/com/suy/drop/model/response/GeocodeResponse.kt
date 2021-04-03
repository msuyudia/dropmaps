package com.suy.drop.model.response

import com.squareup.moshi.Json
import com.suy.drop.model.data.GeocodeResult

data class GeocodeResponse(
    @Json(name = "results") val results: List<GeocodeResult>? = mutableListOf(),
    @Json(name = "status") val status: String? = null
)
