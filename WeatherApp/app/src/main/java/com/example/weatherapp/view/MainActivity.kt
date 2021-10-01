package com.example.weatherapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.weatherapp.view.databinding.MainActivityBinding
import java.io.IOException

private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainActivity : AppCompatActivity() {
    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when {
                it -> getContact()
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.READ_CONTACTS
                ) -> {
                    Toast.makeText(
                        this,
                        "Go to app settings and enable permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Toast.makeText(this, ":(", Toast.LENGTH_SHORT).show()
            }
        }

    private val permissionGeoResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            when {
                it -> getLocation()
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    Toast.makeText(
                        this,
                        "Go to app settings and enable permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> Toast.makeText(this, ":(", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.getProvider(LocationManager.GPS_PROVIDER)?.let {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    REFRESH_PERIOD,
                    MINIMAL_DISTANCE,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            getAddressByLocation(location)
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            //doNothing
                            //Если писать на новом API, то один из методов устарел(OnStatusChanged).
                            //Программа может упасть. Поэтому тело переопределённой функции очищаем
                        }

                        override fun onProviderEnabled(provider: String) {

                        }

                        override fun onProviderDisabled(provider: String) {

                        }
                    }
                )
            }
        } else {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

    }

    private fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(this)

        Thread {
            try {
                val address = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                //post - способо передать данные из неосновоного потока в основной
                binding.container.post {
                    AlertDialog.Builder(this)
                        .setMessage(address[0].getAddressLine(0))
                        .setCancelable(true)
                        .show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow() // синхронное добавление
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.id_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.get_contacts -> {
                permissionResult.launch(android.Manifest.permission.READ_CONTACTS)
                true
            }
            R.id.get_location -> {
                permissionGeoResult.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                true
            }
            R.id.show_map -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, MapsFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getContact() {
        contentResolver

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        val contacts = mutableListOf<String>()
        cursor?.let {
            for (i in 0..cursor.count) {
                // Переходим на позицию в Cursor
                if (cursor.moveToPosition(i)) {
                    // Берём из Cursor столбец с именем
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    contacts.add(name)
                }
            }
        }
        AlertDialog.Builder(this).setItems(contacts.toTypedArray(), { d, w -> })
            .setCancelable(true)
            .show()

    }

}