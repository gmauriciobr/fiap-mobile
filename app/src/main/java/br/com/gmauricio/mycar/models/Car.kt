package br.com.gmauricio.mycar.models

data class Car(
    var id: String? = null,
    val marca: String? = null,
    val modelo: String? = null,
    val ano: Double? = null,
    val valor: Double? = null,
)
