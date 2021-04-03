package com.suy.drop.model.response

import com.suy.drop.model.data.AutoCompleteResult

data class AutoCompleteResponse(
    val predictions: List<AutoCompleteResult>? = mutableListOf(),
    val status: String? = null
)
