package com.example.olsera.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.adevinta.leku.*
import com.example.olsera.R
import com.example.olsera.databinding.ActivityInputCompanyBinding
import com.example.olsera.db.Company
import com.example.olsera.utils.showCustomToast
import com.example.olsera.viewmodel.CompanyViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InputCompany : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var binding: ActivityInputCompanyBinding
    private var selectedRadioButton: String? = ""
    private lateinit var viewModel: CompanyViewModel
    private var data: Company? = null
    private var companyID = -1
    private var editType = false
    private var listener: ((Boolean) -> Unit?)? = null

    private lateinit var mapView: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude = -7.779219
    private var longitude = 110.378007

    companion object {
        const val MAPS_API_KEY = "AIzaSyCuEkBxn6JCa0_M56a31MoXoq2UJ6jkvBI"
    }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[CompanyViewModel::class.java]

        val companyType = intent.getStringExtra("companyType")
        if (companyType.equals("Edit")) {
            editType = true
            val bundle = intent.extras!!
            if (bundle.getParcelable<Company>("company") !== null) {
                data = bundle.getParcelable<Company>("company")!!
            }
            companyID = intent.getIntExtra("companyID", -1)
            binding.tvHead.text = "Edit Location"
            binding.btnSave.text = "Update"
            binding.btnDelete.visibility = View.VISIBLE
            (binding.etCompanyName as TextView).text = data!!.companyName
            (binding.etCompanyAddress as TextView).text = data!!.address
            (binding.etCompanyCity as TextView).text = data!!.city
            (binding.etCompanyZipCode as TextView).text = data!!.zipCode
            latitude = data!!.latitude
            longitude = data!!.longitude
            if (data!!.status == "Active") {
                binding.rbActive.isChecked = true
            } else {
                binding.rbInactive.isChecked = true
            }
            selectedRadioButton = data!!.status
        } else {
            binding.btnSave.text = "Save"
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        with(binding) {

            closeBtn.setOnClickListener { onBackPressed() }

            btnChangeMaps.setOnClickListener {
                pickLocation()
            }

            btnCancel.setOnClickListener {
                (binding.etCompanyName as TextView).text = ""
                (binding.etCompanyAddress as TextView).text = ""
                (binding.etCompanyCity as TextView).text = ""
                (binding.etCompanyZipCode as TextView).text = ""
            }

            btnDelete.setOnClickListener {
//                listener!!(true)
                Toast(this@InputCompany).showCustomToast(
                    "Delete Company Successfull",
                    this@InputCompany
                )
                val intent = Intent()
                setResult(2, intent)
                finish()
            }

            btnSave.setOnClickListener {
                if (validateProductDetails()) {
                    val companyName = binding.etCompanyName.text.toString().trim { it <= ' ' }
                    val companyAddress = binding.etCompanyAddress.text.toString().trim { it <= ' ' }
                    val companyCity = binding.etCompanyCity.text.toString().trim { it <= ' ' }
                    val companyZipcode = binding.etCompanyZipCode.text.toString().trim { it <= ' ' }
                    val dCompany = Company(
                        selectedRadioButton!!,
                        companyName,
                        companyAddress,
                        companyCity,
                        latitude,
                        longitude,
                        companyZipcode
                    )

                    if (companyType.equals("Edit")) {

                        dCompany.companyID = companyID
                        viewModel.updateCompany(dCompany)
                        Toast(this@InputCompany).showCustomToast(
                            "Update Company Successfull",
                            this@InputCompany
                        )

                    } else {
                        viewModel.addCompany(dCompany)
                        Toast(this@InputCompany).showCustomToast(
                            "Add Company Successfull",
                            this@InputCompany
                        )
                    }

                    startActivity(Intent(this@InputCompany, MainActivity::class.java))
                    finish()

                }
            }

            rgStatus.setOnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                selectedRadioButton = radio.text.toString()
            }

        }

    }

    private fun validateProductDetails(): Boolean {
        return when {

            selectedRadioButton!!.isEmpty() || selectedRadioButton!! == "" -> {
                Toast(this).showCustomToast("Status tidak boleh kosong", this)
                false
            }

            TextUtils.isEmpty(binding.etCompanyName.text.toString().trim { it <= ' ' }) -> {
                Toast(this).showCustomToast("Name tidak boleh kosong", this)
                false
            }

            TextUtils.isEmpty(binding.etCompanyAddress.text.toString().trim { it <= ' ' }) -> {
                Toast(this).showCustomToast("Address tidak boleh kosong", this)
                false
            }

            TextUtils.isEmpty(binding.etCompanyCity.text.toString().trim { it <= ' ' }) -> {
                Toast(this).showCustomToast("City tidak boleh kosong", this)
                false
            }

            binding.etCompanyZipCode.length() < 1 -> {
                Toast(this).showCustomToast("Zipcode tidak boleh kosong", this)
                false
            }

            else -> {
                true
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapView = googleMap
        mapView.uiSettings.isZoomControlsEnabled = true
        mapView.setOnMarkerClickListener(this)
        setUpMap()
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        mapView.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                latitude =  if(editType) latitude else location.latitude
                longitude = if(editType) longitude else location.longitude
                val currentLatLong =
                    com.google.android.gms.maps.model.LatLng(if(editType) latitude else location.latitude, if(editType) longitude else location.longitude)
                placeMarkerOnMap(currentLatLong)
                mapView.animateCamera(
                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                        currentLatLong,
                        15f
                    )
                )
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: com.google.android.gms.maps.model.LatLng) {
        val markerOptions =
            com.google.android.gms.maps.model.MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mapView.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker): Boolean = false

    private fun pickLocation() {
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withGooglePlacesApiKey(MAPS_API_KEY)
            .withLocation(latitude, longitude)
            .withGeolocApiKey(MAPS_API_KEY)
            .withSearchZone("id_ID")
            .shouldReturnOkOnBackPressed()
            .withSatelliteViewHidden()
            .withGoogleTimeZoneEnabled()
            .withVoiceSearchHidden()
            .withUnnamedRoadHidden()
            .build(applicationContext)

        startActivityForResult(locationPickerIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.e("MASUK", requestCode.toString())
            if (requestCode == 1) {
                val lat = data.getDoubleExtra(LATITUDE, 0.0)
                val long = data.getDoubleExtra(LONGITUDE, 0.0)
                val postalcode = data.getStringExtra(ZIPCODE)
                val fullAddress = data.getParcelableExtra<Address>(ADDRESS)
                latitude = lat
                longitude = long
                (binding.etCompanyAddress as TextView).text = fullAddress!!.getAddressLine(0)
                (binding.etCompanyCity as TextView).text = fullAddress.subAdminArea
                (binding.etCompanyZipCode as TextView).text = postalcode
                val newLatLong =
                    com.google.android.gms.maps.model.LatLng(latitude, longitude)
                placeMarkerOnMap(newLatLong)
                mapView.animateCamera(
                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                        newLatLong,
                        15f
                    )
                )
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("RESULT****", "CANCELLED")
        }
    }

//    companion object {
//        fun newInstance(listener: ((Boolean) -> Unit)): InputCompany {
//            val inputCompany = InputCompany()
//            inputCompany.listener = listener
//            return inputCompany
//        }
//    }

}