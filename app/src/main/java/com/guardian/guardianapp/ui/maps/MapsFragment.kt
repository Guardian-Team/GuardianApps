package com.guardian.guardianapp.ui.maps

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.guardian.guardianapp.MainViewModel
import com.guardian.guardianapp.R
import com.guardian.guardianapp.databinding.FragmentMapsBinding
import com.guardian.guardianapp.model.UserPreference
import com.guardian.guardianapp.model.ViewModelFactory
import com.guardian.guardianapp.ui.HomeActivity
import com.guardian.guardianapp.ui.RegisterContactsActivity
import com.guardian.guardianapp.utils.Helper

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var mMap: GoogleMap
    private lateinit var lastLoc: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val listContact = ArrayList<String>()
    private var msgSos: String = "Help me I am In Danger!"


    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        getMyLocation()
        mMap.setOnMarkerClickListener(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        setViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setViewModel() {
        val pref = UserPreference.getInstance((activity as HomeActivity).getDataStore())
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[MainViewModel::class.java]

        viewModel.getUser().observe(viewLifecycleOwner) {
            viewModel.getListContactUser(it.userid, it.token)
            viewModel.itemUserContact.observe(viewLifecycleOwner) {
                for (idx in it) {
                    listContact.add(idx.phone)
                }
            }
            viewModel.getUserSettings().observe(viewLifecycleOwner){
                msgSos = it.msgSos
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) {
                if (it != null) {
                    lastLoc = it
                    val currentLatLong = LatLng(it.latitude, it.longitude)
                    placeMarker(currentLatLong)
                    binding.btnShare.setOnClickListener {
                        sendMyLocation(currentLatLong)
                    }
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun sendMyLocation(currentLatLong: LatLng) {
        val msg: SmsManager = SmsManager.getDefault()
        var idx = 0
        if (listContact.isNotEmpty()){
            while (idx < listContact.size){
                msg.sendTextMessage(
                    "+62${listContact[idx]}",
                    null,
                    "${msgSos} \n\n http://maps.google.com/?q=${currentLatLong.latitude},${currentLatLong.longitude}",
                    null,
                    null
                )
                idx += 1
            }
            Helper.showToastLong(requireActivity(), getString(R.string.location_sending_success))
        }else{
            Helper.showToastLong(requireActivity(), "Please add Registered Phone Number First!")
            startActivity(Intent(activity, RegisterContactsActivity::class.java))
        }

    }

    private fun placeMarker(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker) = false

}