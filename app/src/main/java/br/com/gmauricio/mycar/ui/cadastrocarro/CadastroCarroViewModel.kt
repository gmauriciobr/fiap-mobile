package br.com.gmauricio.mycar.ui.cadastrocarro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gmauricio.mycar.constants.Const
import br.com.gmauricio.mycar.models.Car
import br.com.gmauricio.mycar.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CadastroCarroViewModel : ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()

    val carState = MutableLiveData<RequestState<Car>>()

    val carEditState = MutableLiveData<RequestState<Car>>()

    fun cadastraCarro(
        id: String?,
        marca: String,
        modelo: String,
        ano: Long,
        valor: Double,
    ) {
        carState.value = RequestState.Loading
        val car = Car(
            id = id,
            marca = marca,
            modelo = modelo,
            ano = ano,
            valor = valor,
            email =  mAuth.currentUser?.email ?: "",
        )
        if (car.id != null) {
            updateCar(car)
        } else {
            createCar(car)
        }

    }

    private fun createCar(car: Car) {
        db.collection(Const.PATH_COLLECTION)
            .add(car)
            .addOnSuccessListener { documentReference ->
                carState.value = RequestState.Success(car)
            }
            .addOnFailureListener { e ->
                carState.value = RequestState.Error(Throwable(e.message))
            }
    }

    private fun updateCar(car: Car) {
        db.collection("cars")
            .document(car.id ?: "")
            .set(car)
            .addOnSuccessListener { documentReference ->
                carState.value = RequestState.Success(car)
            }
            .addOnFailureListener { e ->
                carState.value = RequestState.Error(Throwable(e.message))
            }
    }

    fun findToeditCar(strId: String) {
        carEditState.value = RequestState.Loading
        db.collection(Const.PATH_COLLECTION)
            .document(strId)
            .get()
            .addOnSuccessListener { documentReference ->
                var car = Car(
                    id = documentReference.id,
                    marca = documentReference["marca"] as String,
                    modelo = documentReference["modelo"] as String,
                    ano = documentReference["ano"] as Long,
                    valor = documentReference["valor"] as Double
                )
                carEditState.value = RequestState.Success(car)
            }
            .addOnFailureListener { e ->
                carEditState.value = RequestState.Error(Throwable(e.message))
            }
    }
}
