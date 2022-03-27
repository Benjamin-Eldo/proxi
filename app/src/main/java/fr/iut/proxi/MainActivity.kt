package fr.iut.proxi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import fr.iut.proxi.model.Coordinate
import fr.iut.proxi.model.LinkBuilder
import fr.iut.proxi.model.PublicEvent

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    private var eventList = ArrayList<PublicEvent>()
    private lateinit var eventAdapter : EventAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView


    val KEY_FROM_MAIN = "KeyMain"

    var builder = LinkBuilder("evenements-publics-cibul")

    var canGetPosition = false

    var userPosition = Coordinate(10F, 10F)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.listEvent)


        val sharedPreferences = this?.getSharedPreferences("proxi", Context.MODE_PRIVATE)

        //Link Builder Setting up
        builder.addParameter("rows", sharedPreferences.getInt("Number of event displayed",10).toString())



        getLocation()

        if (supportActionBar != null){
            supportActionBar!!.hide()
        }


        Log.d("MainActivity",userPosition.toString())


        if (checkForInternet(this)){
            Log.d("MainActivity",builder.build())
            AsyncTaskGetData().execute(builder.build(),this)
        }
        else{
            val alertBuilder = AlertDialog.Builder(this)
            with (alertBuilder){
                setTitle("No Internet")
                setMessage("You don't have Internet")
                show()
            }
        }
        Log.d("MainActivity","EventList Size :"+ eventList.size)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    override fun onRestart() {
        super.onRestart()
        val sharedPreferences = this?.getSharedPreferences("proxi", Context.MODE_PRIVATE)

        //Link Builder Setting up
        builder.addParameter("rows", sharedPreferences.getInt("Number of event displayed",10).toString())

        if (checkForInternet(this)){
            AsyncTaskGetData().execute(builder.build(),this)
        }
        else{
            val alertBuilder = AlertDialog.Builder(this)
            with (alertBuilder){
                setTitle("No Internet")
                setMessage("You don't have Internet")
                show()
            }
        }
    }

    fun onSearchClick(view: View){
        if (checkForInternet(this)){
            AsyncTaskGetData().execute(builder.build(),this)
        }
        else{
            val alertBuilder = AlertDialog.Builder(this)
            with (alertBuilder){
                setTitle("No Internet")
                setMessage("You don't have Internet")
                show()
            }
        }
    }

    fun onMapClick(view:View){

        if (checkForInternet(this)){
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra(KEY_FROM_MAIN, eventList)
            startActivityForResult(intent, 1)
        }
        else{
            val alertBuilder = AlertDialog.Builder(this)
            with (alertBuilder){
                setTitle("No Internet")
                setMessage("You don't have Internet")
                show()
            }
        }



    }
    fun onSettingClick(view:View){
        Log.d("MainActivity","onSettingClick")
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }


    fun partItemClicked(partItem:PublicEvent) {
        val intent = Intent(this, EventDescriptionActivity::class.java)
        intent.putExtra(KEY_FROM_MAIN, partItem)
        startActivityForResult(intent, 2)
    }


    private fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    canGetPosition = true
                } else {
                    canGetPosition = false
                }
            }
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        Log.d("getLocation","before asking perm")
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(p0: Location) {
        userPosition.lat = p0.latitude.toFloat()
        userPosition.lon = p0.longitude.toFloat()
        builder.addParameter("geofilter.distance",userPosition.lat.toString()+"%2C"+userPosition.lon.toString()+"%2C"+"1000000")
        AsyncTaskGetData().execute(builder.build(),this)
    }

    fun getEventList():ArrayList<PublicEvent>{
        return this.eventList
    }

    fun getEventAdapter():EventAdapter{
        return this.eventAdapter
    }

    fun getLayoutManager():RecyclerView.LayoutManager{
        return this.layoutManager
    }

    fun getRecyclerView():RecyclerView{
        return this.recyclerView
    }

    fun setEventAdapter(eventAdapter: EventAdapter){
        this.eventAdapter = eventAdapter
    }

    fun setLayoutManager(layoutManager: LinearLayoutManager){
        this.layoutManager= layoutManager
    }

    fun setEventList(eventList : ArrayList<PublicEvent>){
        this.eventList = eventList
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    override fun onProviderEnabled(provider: String) {

    }


}