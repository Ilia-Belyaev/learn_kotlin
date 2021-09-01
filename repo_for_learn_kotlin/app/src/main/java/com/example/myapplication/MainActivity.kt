package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import java.util.*

lateinit var text: Something
private var array = Array(10) { i -> i * i }

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            Toast.makeText(
                this,
                Something("Hello,Potter. How many years gone, ", 2, "?").doSomething(),
                Toast.LENGTH_SHORT
            ).show()
        }

        text = Something("Hello,Potter. How many years gone, ", 2, "?")

        findViewById<Button>(R.id.button_2).setOnClickListener {
            Toast.makeText(this, text.copy(somethingSecond = 10).doSomething(), Toast.LENGTH_SHORT)
                .show()
        }

        findViewById<Button>(R.id.button_3).setOnClickListener {
            Toast.makeText(this, array.contentToString(), Toast.LENGTH_SHORT).show()
        }
    }
}

data class Something(
    val somethingFirst: String,
    var somethingSecond: Int,
    var somethingThird: String
) {

    fun doSomething(): String {
        this.somethingSecond = somethingSecond * somethingSecond
        return "$somethingFirst $somethingSecond $somethingThird"
    }


}