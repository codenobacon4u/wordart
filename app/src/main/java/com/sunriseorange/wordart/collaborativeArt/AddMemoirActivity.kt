package com.sunriseorange.wordart.collaborativeArt

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sunriseorange.wordart.R
import java.io.IOException
import java.util.*

class AddMemoirActivity : FragmentActivity(), OnMapReadyCallback {
    companion object {
        private const val TAG = "Collaborative-Art-Project"
        private const val CAMERA_LNG = 100.0
        private const val CAMERA_LAT = 28.0
    }

    private lateinit var editTextMemoirs: EditText
    private lateinit var editTextAuthor: EditText
    private lateinit var editTextLocation: EditText
    private lateinit var saveButton: Button

    private lateinit var databaseMemoirs: DatabaseReference

    private var mMap: GoogleMap? = null
    private var mLatLng: LatLng? = null
    private var mMapReady = false
    private var mLocationValid = false

    // Super class for a memoir post
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_memoir)

        editTextMemoirs = findViewById(R.id.editTextMemoir)
        editTextAuthor = findViewById(R.id.editTextAuthor)
        editTextLocation = findViewById(R.id.editTextLocation)
        saveButton = findViewById(R.id.button)

        val map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        map?.getMapAsync(this)

        databaseMemoirs = FirebaseDatabase.getInstance().getReference("memoirs")

        editTextLocation.setOnFocusChangeListener { _: View, hasFocus: Boolean ->
            if(!hasFocus) {
                Log.i(TAG, "doAfterTextChanged")
                    updateMap(editTextLocation.text.toString())
            }
        }

        saveButton.setOnClickListener {
            val text = editTextMemoirs.text
            val author = editTextAuthor.text
            val location = editTextLocation.text
            when {
                text.isBlank() -> Toast.makeText(this, "Please enter a memoir", Toast.LENGTH_SHORT).show()
                author.isBlank() -> Toast.makeText(this, "Please enter an author name", Toast.LENGTH_SHORT).show()
                location.isBlank() || !mLocationValid -> Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show()
                text.split(" ").size != 6 -> Toast.makeText(this, "Each memoir must be six words, no more, no less!", Toast.LENGTH_LONG).show()
                else -> addMemoir()
            }
        }
    }

    // Saves map location that user decided
    private fun updateMap(location : String) {
        Log.i(TAG, "updateMap")
        var errorMessage = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address> = emptyList()

        try {
            addresses = geocoder.getFromLocationName(location, 1)
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
            mLocationValid = false
        } else {
            val address = addresses[0]

            mLocationValid = true
            mLatLng = LatLng(address.latitude, address.longitude)

            mMap!!.setMinZoomPreference(4.0f)
            mMap!!.setMaxZoomPreference(14.0f)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(mLatLng!!))
            mMap!!.addMarker(MarkerOptions().position(mLatLng!!))
        }
    }

    // Save the memoir that user choose (6 words)
    private fun addMemoir() {
        val text = editTextMemoirs.text
        val author = editTextAuthor.text.toString().trim { it <= ' ' }
        val location = editTextLocation.text.toString().trim { it <= ' ' }

        val id = databaseMemoirs.push().key

        val memoir = Memoir(id!!, author, text.toString(), location)

        databaseMemoirs.child(id).setValue(memoir)

        Toast.makeText(this, "Memoir Added", Toast.LENGTH_LONG).show()
        finish()
    }

    // Puts map back to the original pane
    override fun onMapReady(googleMap: GoogleMap) {
        Log.i(TAG, "onMapReady")
        mMapReady = true
        mMap = googleMap
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(1.0f))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(CAMERA_LAT, CAMERA_LNG)))
    }
}