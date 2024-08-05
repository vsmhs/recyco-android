package com.ukmprogramming.recyco.ui.activities.location

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.ukmprogramming.recyco.R
import com.ukmprogramming.recyco.databinding.ActivityLocationBinding
import com.ukmprogramming.recyco.util.ResultState
import com.ukmprogramming.recyco.util.handleHttpException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationBinding

    private val viewModel by viewModels<LocationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.treatment_location)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener {
            it.showInfoWindow()
            false
        }

        binding.apply {
            viewModel.locationDataState.observe(this@LocationActivity) { resultState ->
                progressBar.isVisible = resultState is ResultState.Loading

                if (resultState is ResultState.Success) {
                    val latLngBoundsBuilder = LatLngBounds.Builder()

                    resultState.data.map {
                        val coordinate = LatLng(it.lat, it.lon)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(coordinate)
                                .title(it.title)
                                .snippet(it.address)
                        )
                        latLngBoundsBuilder.include(coordinate)
                    }

                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            latLngBoundsBuilder.build(), 50
                        )
                    )
                } else if (resultState is ResultState.Error) {
                    resultState.exception.getData()?.handleHttpException()?.let { message ->
                        Toast.makeText(this@LocationActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

        viewModel.getForumPosts()
    }
}