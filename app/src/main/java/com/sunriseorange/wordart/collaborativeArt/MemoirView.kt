package com.sunriseorange.wordart.collaborativeArt

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sunriseorange.wordart.R
import java.io.IOException
import java.lang.IllegalArgumentException
import java.util.*

class MemoirView : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        private const val TAG = "Collaborative-Art-Project"
    }

    private lateinit var author : TextView
    private lateinit var memoir : TextView
    private lateinit var location : TextView

    private var mMap: GoogleMap? = null
    private var mLatLng: LatLng? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memoir_view)

        val intent = intent

        author = findViewById(R.id.authorText)
        memoir = findViewById(R.id.memoirText)
        location = findViewById(R.id.locationText)

        author.text = intent.getStringExtra("user")
        memoir.text = intent.getStringExtra("memoir")
        location.text = intent.getStringExtra("location")

        val map = supportFragmentManager.findFragmentById(R.id.displayMap) as SupportMapFragment?
        map?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(TAG, "onMapReady")

        var errorMessage = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocationName(intent.getStringExtra("location"), 1)
        } catch (ioException: IOException) {
            errorMessage = getString(R.string.service_not_available)
            Log.e(TAG, errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            errorMessage = getString(R.string.invalid_location)
            Log.e(TAG, "$errorMessage. Location = $location", illegalArgumentException)
        }

        if (addresses.isEmpty()) {
            if (errorMessage.isEmpty()) {
                Log.e(TAG, getString(R.string.no_address_found))
                Toast.makeText(this, getString(R.string.no_address_found), Toast.LENGTH_LONG).show()
            }
        } else {
            val address = addresses[0]
            Log.i(TAG, addresses.toString())
            mLatLng = LatLng(address.latitude, address.longitude)
        }

        mMap = googleMap
        mMap!!.setMinZoomPreference(4.0f)
        mMap!!.setMaxZoomPreference(14.0f)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(mLatLng!!))
        mMap!!.addMarker(MarkerOptions().position(mLatLng!!))
    }
}