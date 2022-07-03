package br.com.gmauricio.mycar.extensions

import android.widget.EditText

fun EditText.getLong(): Long {
    var value = this.getString()
    if (value.isNotBlank()) {
        return value.toLong()
    }
    return 0
}

fun EditText.getDouble(): Double {
    var value = this.getString()
    if (value.isNotBlank()) {
        return value.toDouble()
    }
    return 0.0
}

fun EditText.getString(): String {
    return this.text.toString()
}
