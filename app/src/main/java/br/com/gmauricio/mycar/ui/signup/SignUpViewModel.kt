package br.com.gmauricio.mycar.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.gmauricio.mycar.models.NewUser
import br.com.gmauricio.mycar.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpViewModel :ViewModel() {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val db = FirebaseFirestore.getInstance()

    val signUpState = MutableLiveData<RequestState<FirebaseUser>>()

    fun signUp(newUser: NewUser) {
        signUpState.value = RequestState.Loading

        if (validateFields(newUser)) {
            mAuth.createUserWithEmailAndPassword(
            newUser.email ?: "",
            newUser.password ?: "" )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    saveInFirestore(newUser) }
                else {
                    signUpState.value = RequestState.Error( Throwable(
                        task.exception?.message ?: "Não foi possível realizar a requisição")
                ) }
            }
        }
    }

    private fun saveInFirestore(newUser: NewUser) {
        db.collection("users")
        //.document(FirebaseAuth.getInstance().currentUser?.uid!!)
        .add(newUser)
        .addOnSuccessListener { documentReference ->
            sendEmailVerification()
        }
        .addOnFailureListener { e ->
            signUpState.value = RequestState.Error( Throwable(e.message)
            )
        }
    }
    private fun sendEmailVerification() {
        mAuth.currentUser?.sendEmailVerification() ?.addOnCompleteListener { task ->
            signUpState.value = RequestState.Success(mAuth.currentUser!!) }
    }

    private fun validateFields(newUser: NewUser): Boolean {
        if (newUser.username?.isEmpty() == true) {
            signUpState.value = RequestState.Error(Throwable("Informe o nome do usuário"))
            return false
        }
        if (newUser.email?.isNotEmpty() == false) {
            signUpState.value = RequestState.Error(Throwable("E-mail inválido"))
            return false
        }

        if (newUser.phone?.isEmpty() == true) {
            signUpState.value = RequestState.Error(Throwable("Informe o telefone do usuário"))
            return false
        }
        if (newUser.password?.isEmpty() == true) {
            signUpState.value = RequestState.Error(Throwable("Informe uma senha"))
            return false
        }
        if (newUser.password?.length ?: 0 < 6) { signUpState.value =
            RequestState.Error(Throwable("Senha com no mínimo 6 caracteres"))
            return false
        }
        return true
    }

}