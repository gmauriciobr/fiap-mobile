package br.com.gmauricio.mycar.extensions

import android.widget.EditText

    fun EditText.getLong() : Long {
        return this.getString().toLong()
    }

    fun EditText.getDouble() : Double {
        return this.getString().toDouble()
    }

    fun EditText.getString(): String {
        return this.text.toString()
    }
