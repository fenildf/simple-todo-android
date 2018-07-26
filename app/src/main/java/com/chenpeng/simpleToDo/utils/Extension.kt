package com.chenpeng.simpleToDo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.chenpeng.simpleToDo.R
import com.irozon.sneaker.Sneaker

/**
 * author : ChenPeng
 * date : 2018/4/13
 * description :
 */
fun Context.toast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun snackbarSuccess(view: View, text: String): Snackbar {
    val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
    val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    snackbar.view.layoutParams = params

    snackbar.view.background = snackbar.context.resources
            .getDrawable(R.drawable.bg_snackbar_success)

    ViewCompat.setElevation(snackbar.view, 6f)

    return snackbar
}

fun snackbarError(view: View, text: String): Snackbar {
    val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
    val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    snackbar.view.layoutParams = params

    snackbar.view.background = snackbar.context.resources.getDrawable(R.drawable.bg_snackbar_error)

    ViewCompat.setElevation(snackbar.view, 6f)

    return snackbar
}

fun <T : Activity> Activity.navTo(clazz: Class<out T>) {
    val intent = Intent(this, clazz)
    this.startActivity(intent)
}

fun Activity.showError(message: String) {
    Sneaker.with(this)
            .setTitle(message, android.R.color.white)
            .setDuration(1000)
            .autoHide(true)
            .sneak(android.R.color.holo_red_light)
}

fun Activity.showSuccess(message: String) {
    Sneaker.with(this)
            .setTitle(message, android.R.color.white)
            .setDuration(1000)
            .autoHide(true)
            .sneak(R.color.colorAccent)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}