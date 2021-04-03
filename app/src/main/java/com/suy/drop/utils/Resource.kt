package com.suy.drop.utils

import com.suy.drop.model.type.Status

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> loading(): Resource<T> = Resource(Status.LOADING, null, null)
        fun <T> success(data: T?): Resource<T> = Resource(Status.SUCCESS, data, null)
        fun <T> error(msg: String): Resource<T> = Resource(Status.ERROR, null, msg)
    }
}
