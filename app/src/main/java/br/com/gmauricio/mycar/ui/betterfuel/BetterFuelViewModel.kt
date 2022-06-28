package br.com.gmauricio.mycar.ui.betterfuel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gmauricio.mycar.models.Car
import br.com.gmauricio.mycar.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BetterFuelViewModel : ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()

    val carState = MutableLiveData<RequestState<Car>>()

    fun calculateBetterFuel(
        marca: String,
        modelo: String,
        ano: Double,
        valor: Double,
    ) {
        carState.value = RequestState.Loading
        val car = Car(
            mAuth.currentUser?.uid ?: "",
            marca,
            modelo,
            ano,
            valor
        )

        db.collection("cars")
//            .document(mAuth.currentUser?.uid ?: "")
//            .set(car)
            .add(car)
            .addOnSuccessListener { documentReference ->
                carState.value = RequestState.Success(car)
            }
            .addOnFailureListener { e ->
                carState.value = RequestState.Error(Throwable(e.message))
            }
    }


}