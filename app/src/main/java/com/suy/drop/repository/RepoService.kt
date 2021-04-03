package com.suy.drop.repository

import com.suy.drop.model.response.AutoCompleteResponse
import com.suy.drop.model.response.GeocodeResponse
import com.suy.drop.model.response.PlaceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoService {
    @GET("geocode/json")
    suspend fun getCurrentLocation(
        @Query("latlng") latLng: String,
        @Query("language") language: String,
        @Query("region") region: String,
        @Query("key") key: String
    ): Response<GeocodeResponse>

    @GET("place/details/json")
    suspend fun getPlaceDetail(
        @Query("place_id") placeId: String,
        @Query("language") language: String,
        @Query("region") region: String,
        @Query("fields") fields: String,
        @Query("key") key: String
    ): Response<PlaceResponse>

    @GET("place/autocomplete/json")
    suspend fun getAutoComplete(
        @Query("input") input: String,
        @Query("sessiontoken") sessionToken: String,
        @Query("radius") radius: Int,
        @Query("language") language: String,
        @Query("components") components: String,
        @Query("key") apiKey: String
    ): Response<AutoCompleteResponse>
}