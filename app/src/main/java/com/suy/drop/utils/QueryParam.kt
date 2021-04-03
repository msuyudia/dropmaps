package com.suy.drop.utils

import com.suy.drop.BuildConfig

object QueryParam {
    const val API_KEY = BuildConfig.API_KEY
    const val ID = "id"
    const val FIELDS = "address_component,name,geometry,formatted_address"
    const val SESSIONTOKEN = "DEVICE_ID"
    const val RADIUS = 1
    const val COMPONENTS = "country:id"
}