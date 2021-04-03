package com.suy.drop.model.data

import com.squareup.moshi.Json

data class PlaceResult(
    @Json(name = "address_components") val addressComponents: List<AddressComponent>? = mutableListOf(),
    val name: String? = null,
    val geometry: Geometry? = null
) {
    companion object {
        private val listType = listOf(
            "administrative_area_level_4",
            "administrative_area_level_3",
            "administrative_area_level_2",
            "administrative_area_level_1",
            "postal_code"
        )
    }

    private fun getAvailableTypes(): List<String> {
        val availableList = mutableListOf<String>()
        for (type in listType) {
            addressComponents?.forEach {
                when (it.types?.contains(type)) {
                    true -> {
                        availableList.add(type)
                        return@forEach
                    }
                }
            }
        }
        return availableList
    }

    fun getAddress(): String {
        val address = StringBuilder()
        val typeLastIndex = getAvailableTypes().lastIndex
        for (i in 0..typeLastIndex) {
            addressComponents?.forEach {
                val availableType = getAvailableTypes()[i]
                when (it.types?.contains(availableType)) {
                    true -> {
                        when (availableType.contains(getAvailableTypes()[typeLastIndex])) {
                            false -> address.append("${it.name}, ")
                            true -> address.append(it.name)
                        }
                        return@forEach
                    }
                }
            }
        }
        return address.toString()
    }
}
