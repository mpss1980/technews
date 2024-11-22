package br.com.alura.technewsupdated.ui.activity.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.showError(message: String) {
    Toast.makeText(
        this,
        message, Toast.LENGTH_LONG
    ).show()
}