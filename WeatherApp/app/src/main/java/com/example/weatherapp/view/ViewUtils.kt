package com.example.weatherapp.view

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException

fun View.hide(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    action: (View)->Unit,
    time: Int = Snackbar.LENGTH_INDEFINITE
){
    Snackbar
        .make(this, text, time)
        .setAction(actionText) { action(this) }
        .show()
}

fun View.hideKeyboard():Boolean = try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }catch (e: RuntimeException){
        false
    }
