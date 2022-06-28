package br.com.gmauricio.mycar.ui.betterfuel

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import br.com.gmauricio.mycar.R
import br.com.gmauricio.mycar.extensions.getDouble
import br.com.gmauricio.mycar.extensions.getString
import br.com.gmauricio.mycar.models.RequestState
import br.com.gmauricio.mycar.ui.base.BaseAuthFragment

class BetterFuelFragment : BaseAuthFragment() {

    override val layout: Int = R.layout.fragment_better_fuel

    private val betterFuelViewModel: BetterFuelViewModel by viewModels()

    private lateinit var etMarca: EditText
    private lateinit var etModelo: EditText
    private lateinit var etAno: EditText
    private lateinit var etValor: EditText

    private lateinit var btCalculate: Button
    private lateinit var btClear: TextView

    val args:BetterFuelFragmentArgs by navArgs()

    override fun onViewCreated(view:View, savedInstanceState:Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        registerObserver()
//        betterFuelViewModel.getCar()
    }

    private fun setUpView(view: View) {
        etMarca = view.findViewById(R.id.etMarca)
        etModelo = view.findViewById(R.id.etModelo)
        etAno = view.findViewById(R.id.etAno)
        etValor = view.findViewById(R.id.etValor)
        btCalculate = view.findViewById(R.id.btCalculate)
        btClear = view.findViewById(R.id.btClear)
        setUpListener()
    }

    private fun setUpListener() {
        btCalculate.setOnClickListener {
            betterFuelViewModel.calculateBetterFuel(
                etMarca.getString(),
                etModelo.getString(),
                etAno.getDouble(),
                etValor.getDouble(),
            )
        }

        btClear.setOnClickListener {
            clearFields()
        }
    }

    private fun clearFields() {
        etMarca.setText("")
        etModelo.setText("")
        etAno.setText("")
        etValor.setText("")
    }

    private fun registerObserver() {
        betterFuelViewModel.carState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    hideLoading()
                    showMessage("Carro cadastrado com sucesso!")
                }
                is RequestState.Error -> {
                    hideLoading()
                    showMessage(it.throwable.message)
                }
                is RequestState.Loading -> {
                    showLoading("Aguarde processando...")
                }
            }
        })
    }

}