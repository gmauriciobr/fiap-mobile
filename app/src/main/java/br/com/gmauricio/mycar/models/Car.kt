package br.com.gmauricio.mycar.models

import com.google.firebase.firestore.Exclude

data class Car(

    @get:Exclude var id: String? = null,
    val marca: String? = null,
    val modelo: String? = null,
    val ano: Long? = null,
    val valor: Double? = null,
    val email: String? = null,
    var user: NewUser? = null
)
