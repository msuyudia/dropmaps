package com.suy.drop.model.data

import com.squareup.moshi.Json

data class GeocodeResult(@Json(name = "place_id") val placeId: String? = null)
