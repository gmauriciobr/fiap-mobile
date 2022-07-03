package br.com.gmauricio.mycar.ui.listcars

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gmauricio.mycar.constants.Const
import br.com.gmauricio.mycar.models.Car
import br.com.gmauricio.mycar.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.net.Authenticator

class ListCarsViewModel : ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()

    val carState = MutableLiveData<RequestState<List<Car>>>()

    var deleteState = MutableLiveData<RequestState<Boolean>>()

    var logOutState = MutableLiveData<RequestState<Boolean>>()

    fun findAll() {
        carState.value = RequestState.Loading
        db.collection(Const.PATH_COLLECTION)
            .get()
            .addOnSuccessListener {
                it?.let { snapshot ->
                    val cars = mutableListOf<Car>()
                    for (documento in snapshot.documents) {
                        documento.data?.let {
                            var id: String = documento.id
                            val marca: String = documento["marca"] as String
                            val modelo: String = documento["modelo"] as String
                            val valor: Double = documento["valor"] as Double
                            val ano: Long = documento["ano"] as Long
                            val email: String = documento["email"] as String
                            val car = Car(
                                id = id,
                                marca = marca,
                                modelo = modelo,
                                ano = ano,
                                valor = valor,
                                email = email
                            )
                            cars.add(car)
                        }
                    }
                    carState.value = RequestState.Success(cars)
                }
            }
    }

    fun deleteById(id: String) {
        deleteState.value = RequestState.Loading
        db.collection(Const.PATH_COLLECTION).document(id).delete()
            .addOnCompleteListener {
                findAll()
                deleteState.value = RequestState.Success(true)
            }
            .addOnFailureListener { exception ->
                deleteState.value = RequestState.Error(exception)
            }
    }

    fun logOut() {
        mAuth.signOut()
        logOutState.value = RequestState.Success(true)
    }

}