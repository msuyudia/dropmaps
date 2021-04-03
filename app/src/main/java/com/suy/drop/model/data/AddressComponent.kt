package com.suy.drop.model.data

import com.squareup.moshi.Json

data class AddressComponent(
    @Json(name = "long_name") val name: String? = null,
    val types: List<String>? = mutableListOf()
)