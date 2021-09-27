package com.example.weatherapp.view

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.weatherapp.view.DetailFragment.Companion.newInstance
import com.example.weatherapp.view.databinding.MainActivityBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private  val binding : MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }
    private val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if(it){
            getContact()
        }else{
            Toast.makeText(this,":(",Toast.LENGTH_SHORT).show()
        }
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
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.id_history-> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.get_contacts ->{
                permissionResult.launch(android.Manifest.permission.READ_CONTACTS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getContact(){
        contentResolver

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        )
        val contacts = mutableListOf<String>()
        cursor?.let{
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
        AlertDialog.Builder(this).setItems(contacts.toTypedArray(),{d,w->})
            .setCancelable(true)
            .show()

    }

}