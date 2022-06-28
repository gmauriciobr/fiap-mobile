package br.com.gmauricio.mycar.models

import com.google.firebase.firestore.Exclude

data class NewUser(
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    @get:Exclude val password: String? = null
)
