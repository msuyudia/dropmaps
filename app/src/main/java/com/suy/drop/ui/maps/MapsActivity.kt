package com.suy.drop.ui.maps

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.rationale.createSnackBarRationale
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.suy.drop.R
import com.suy.drop.databinding.ActivityMapsBinding
import com.suy.drop.model.type.Status
import com.suy.drop.ui.autocomplete.AutoCompleteAdapter
import com.suy.drop.ui.autocomplete.AutoCompleteListener
import com.suy.drop.utils.gone
import com.suy.drop.utils.visible
import splitties.toast.longToast

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, AutoCompleteListener {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var map: GoogleMap
    private val fusedLocation by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val currentZoom by lazy { 15F }
    private val minZoom by lazy { 10F }
    private val viewModel by viewModels<MapsViewModel>()
    private var latLng: LatLng? = null
    private var isUISearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initiateViewModel()
        initiateCurrentLocation()
        initiateMap()
        initiateListener()
    }

    private fun initiateViewModel() {
        viewModel.placeLiveData().observe(this, {
            when (it.status) {
                Status.LOADING -> showShimmer()
                Status.SUCCESS -> {
                    hideShimmer()
                    val place = it.data?.result
                    if (isUISearch) {
                        val location = place?.geometry?.location ?: return@observe
                        if (location.lat != null && location.lng != null) {
                            val latLng = LatLng(location.lat, location.lng)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, currentZoom))
                        }
                    }
                    showUILocation(place?.name, place?.getAddress())
                }
                Status.ERROR -> {
                    hideShimmer()
                    binding.tvPlaceName.text = getString(R.string.text_failed_load_address)
                    longToast(it.message ?: "")
                }
            }
        })
        viewModel.autoCompleteLiveData().observe(this, {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS ->
                    binding.rvPlaces.adapter = AutoCompleteAdapter(it.data ?: mutableListOf(), this)
                Status.ERROR ->
                    longToast(it.message ?: "")
            }
        })
    }

    private fun initiateMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun initiateCurrentLocation() {
        askForPermissions(
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION,
            rationaleHandler = rationaleHandler
        ) { result ->
            with(result) {
                when {
                    isAllDenied(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION
                    ) ->
                        initiateCurrentLocation()
                    isAllGranted(
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.ACCESS_COARSE_LOCATION
                    ) -> {
                        fusedLocation.lastLocation.addOnSuccessListener { loc ->
                            latLng = LatLng(loc.latitude, loc.longitude)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, currentZoom))
                            viewModel.getPlaceIdFromGeocode(loc.latitude, loc.longitude)
                        }
                    }
                }
            }
        }
    }

    private val rationaleHandler by lazy {
        createSnackBarRationale(binding.root) {
            onPermission(Permission.ACCESS_FINE_LOCATION, R.string.text_accept_location_permission)
            onPermission(
                Permission.ACCESS_COARSE_LOCATION,
                R.string.text_accept_location_permission
            )
        }
    }

    private fun initiateListener() {
        with(binding) {
            btnPickAddress.setOnClickListener {
                longToast("${binding.tvPlaceName.text}, ${binding.tvPlaceAddress.text}")
            }
            btnBack.setOnClickListener {
                when (isUISearch) {
                    true -> {
                        isUISearch = false
                        backToUIMaps()
                    }
                    false -> super.onBackPressed()
                }
            }
            btnEndToolbar.setOnClickListener {
                btnEndToolbar.gone()
                btnBack.setImageResource(R.drawable.ic_clear)
                containerSearch.visible()
                toolbar.subtitle = getString(R.string.text_search_address)
                isUISearch = true
            }
            svPlaces.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    viewModel.searchAddress(query)
                    return true
                }
            })
        }
    }

    override fun onBackPressed() {
        when (isUISearch) {
            true -> backToUIMaps()
            false -> super.onBackPressed()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.setMinZoomPreference(minZoom)
        map.setOnCameraIdleListener {
            val target = map.cameraPosition.target
            when (isUISearch) {
                false -> viewModel.getPlaceIdFromGeocode(target.latitude, target.longitude)
                true -> isUISearch = false
            }
        }
    }

    override fun onPlaceClicked(placeId: String?) {
        backToUIMaps()
        viewModel.getPlaceDetail(placeId)
    }

    private fun showUILocation(name: String?, address: String?) {
        with(binding) {
            tvPlaceName.text = name ?: getString(R.string.text_name_not_found)
            tvPlaceAddress.text = address ?: getString(R.string.text_address_not_found)
            btnPickAddress.isEnabled = true
        }
    }

    private fun showShimmer() {
        with(binding) {
            tvPlaceName.text = getString(R.string.text_loading)
            tvPlaceAddress.text = ""
            btnPickAddress.isEnabled = false
            shimmer.showShimmer(true)
            shimmer.startShimmer()
        }
    }

    private fun hideShimmer() {
        with(binding) {
            shimmer.stopShimmer()
            shimmer.hideShimmer()
        }
    }

    private fun backToUIMaps() {
        with(binding) {
            toolbar.subtitle = getString(R.string.text_pick_address)
            containerSearch.gone()
            btnBack.setImageResource(R.drawable.ic_back)
            btnEndToolbar.visible()
        }
    }
}