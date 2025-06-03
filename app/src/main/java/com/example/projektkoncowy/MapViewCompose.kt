package com.example.projektkoncowy.ui.map

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                mapView.onCreate(Bundle())
            }

            override fun onStart(owner: LifecycleOwner) {
                mapView.onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                mapView.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                mapView.onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                mapView.onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                mapView.onDestroy()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}

@Composable
fun GoogleMapView(
    lat: Double,
    lng: Double,
    modifier: Modifier = Modifier
) {
    val mapView = rememberMapViewWithLifecycle()
    AndroidView(
        factory = { mapView },
        modifier = modifier
    ) { map ->
        map.getMapAsync { googleMap ->
            val location = LatLng(lat, lng)
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
            googleMap.addMarker(
                MarkerOptions().position(location).title("Moja lokalizacja")
            )
        }
    }
}
