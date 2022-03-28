package fr.iut.proxi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fr.iut.proxi.databinding.ActivityMapsBinding
import fr.iut.proxi.model.PublicEvent

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var eventList : ArrayList<PublicEvent>

    val KEY_FROM_MAP = "KeyMap"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bundle: Bundle ?= intent.extras
        bundle?.let{
            bundle.apply {
                eventList = getSerializable("KeyMain") as ArrayList<PublicEvent>
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        for (i in 0 until eventList.size){
            var point = LatLng(eventList.get(i).coordinate.lon.toDouble(),
                eventList.get(i).coordinate.lat.toDouble())
            mMap.addMarker(MarkerOptions().position(point).title(eventList.get(i).name))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 8F))
        }

        // Add a marker in Sydney and move the camera


    }
}