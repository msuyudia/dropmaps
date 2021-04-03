package com.suy.drop.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suy.drop.model.data.AutoCompleteResult
import com.suy.drop.model.response.PlaceResponse
import com.suy.drop.model.type.ResponseType
import com.suy.drop.repository.Repository
import com.suy.drop.utils.QueryParam
import com.suy.drop.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class MapsViewModel : ViewModel() {
    private val place by lazy { MutableLiveData<Resource<PlaceResponse>>() }
    private val autoComplete by lazy { MutableLiveData<Resource<List<AutoCompleteResult>>>() }
    private val noConnection by lazy { place.postValue(Resource.error("Koneksi bermasalah, silahkan cek jaringan Anda")) }
    private val placeIdNotFound by lazy { place.postValue(Resource.error("Place ID tidak ditemukan. Harap hubungi developer")) }
    private var job: Job = Job()

    fun getPlaceIdFromGeocode(lat: Double?, long: Double?) {
        if (lat == null || long == null) {
            place.postValue(Resource.error("Maaf, aplikasi tidak bisa mendapatkan lokasi Anda. Harap nyalakan GPS Anda"))
            return
        }
        place.postValue(Resource.loading())
        job.cancel()
        job = viewModelScope.launch {
            delay(3000)
            val latLng = "$lat,$long"
            try {
                val response = Repository.api.getCurrentLocation(
                    latLng,
                    QueryParam.ID,
                    QueryParam.ID,
                    QueryParam.API_KEY
                ).body()
                when (response?.status) {
                    ResponseType.OK.name -> {
                        val placeId = response.results?.get(0)?.placeId
                        when (placeId != null) {
                            true -> getPlaceDetail(placeId)
                            false -> placeIdNotFound
                        }
                    }
                    ResponseType.ZERO_RESULTS.name ->
                        place.postValue(Resource.error(ResponseType.ZERO_RESULTS.message))
                    ResponseType.OVER_DAILY_LIMIT.name ->
                        place.postValue(Resource.error(ResponseType.OVER_DAILY_LIMIT.message))
                    ResponseType.OVER_QUERY_LIMIT.name ->
                        place.postValue(Resource.error(ResponseType.OVER_QUERY_LIMIT.message))
                    ResponseType.REQUEST_DENIED.name ->
                        place.postValue(Resource.error(ResponseType.REQUEST_DENIED.message))
                    ResponseType.INVALID_REQUEST.name ->
                        place.postValue(Resource.error(ResponseType.INVALID_REQUEST.message))
                    ResponseType.UNKNOWN_ERROR.name ->
                        place.postValue(Resource.error(ResponseType.UNKNOWN_ERROR.message))
                    else -> place.postValue(Resource.error(ResponseType.NULL_ERROR.message))
                }
            } catch (e: IOException) {
                noConnection
            }
        }
        job.start()
    }

    fun searchAddress(input: String?) {
        job.cancel()
        job = viewModelScope.launch {
            delay(1000)
            if (input.isNullOrEmpty()) {
                autoComplete.postValue(Resource.success(mutableListOf()))
                return@launch
            }
            try {
                val response = Repository.api.getAutoComplete(
                    input,
                    QueryParam.SESSIONTOKEN,
                    QueryParam.RADIUS,
                    QueryParam.ID,
                    QueryParam.COMPONENTS,
                    QueryParam.API_KEY
                ).body()
                when (response?.status) {
                    ResponseType.OK.name -> autoComplete.postValue(Resource.success(response.predictions))
                    ResponseType.ZERO_RESULTS.name ->
                        autoComplete.postValue(Resource.error(ResponseType.ZERO_RESULTS.message))
                    ResponseType.OVER_DAILY_LIMIT.name ->
                        autoComplete.postValue(Resource.error(ResponseType.OVER_DAILY_LIMIT.message))
                    ResponseType.OVER_QUERY_LIMIT.name ->
                        autoComplete.postValue(Resource.error(ResponseType.OVER_QUERY_LIMIT.message))
                    ResponseType.REQUEST_DENIED.name ->
                        autoComplete.postValue(Resource.error(ResponseType.REQUEST_DENIED.message))
                    ResponseType.INVALID_REQUEST.name ->
                        autoComplete.postValue(Resource.error(ResponseType.INVALID_REQUEST.message))
                    ResponseType.UNKNOWN_ERROR.name ->
                        autoComplete.postValue(Resource.error(ResponseType.UNKNOWN_ERROR.message))
                    else -> autoComplete.postValue(Resource.error(ResponseType.NULL_ERROR.message))
                }
            } catch (e: IOException) {
                noConnection
            }
        }
        job.start()
    }

    fun getPlaceDetail(placeId: String?) {
        if (placeId == null) {
            placeIdNotFound
            return
        }
        viewModelScope.launch {
            place.postValue(Resource.loading())
            try {
                val response = Repository.api.getPlaceDetail(
                    placeId,
                    QueryParam.ID,
                    QueryParam.ID,
                    QueryParam.FIELDS,
                    QueryParam.API_KEY
                ).body()
                when (response?.status) {
                    ResponseType.OK.name -> place.postValue(Resource.success(response))
                    ResponseType.ZERO_RESULTS.name ->
                        place.postValue(Resource.error(ResponseType.ZERO_RESULTS.message))
                    ResponseType.OVER_DAILY_LIMIT.name ->
                        place.postValue(Resource.error(ResponseType.OVER_DAILY_LIMIT.message))
                    ResponseType.OVER_QUERY_LIMIT.name ->
                        place.postValue(Resource.error(ResponseType.OVER_QUERY_LIMIT.message))
                    ResponseType.REQUEST_DENIED.name ->
                        place.postValue(Resource.error(ResponseType.REQUEST_DENIED.message))
                    ResponseType.INVALID_REQUEST.name ->
                        place.postValue(Resource.error(ResponseType.INVALID_REQUEST.message))
                    ResponseType.UNKNOWN_ERROR.name ->
                        place.postValue(Resource.error(ResponseType.UNKNOWN_ERROR.message))
                    ResponseType.NOT_FOUND.name ->
                        place.postValue(Resource.error(ResponseType.NOT_FOUND.message))
                    else -> place.postValue(Resource.error(ResponseType.NULL_ERROR.message))
                }
            } catch (e: IOException) {
                noConnection
            }
        }
    }

    fun placeLiveData(): LiveData<Resource<PlaceResponse>> = place
    fun autoCompleteLiveData(): LiveData<Resource<List<AutoCompleteResult>>> = autoComplete
}
