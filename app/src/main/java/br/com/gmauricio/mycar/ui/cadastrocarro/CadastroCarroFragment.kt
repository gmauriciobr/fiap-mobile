package br.com.gmauricio.mycar.ui.cadastrocarro

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.gmauricio.mycar.R
import br.com.gmauricio.mycar.extensions.getDouble
import br.com.gmauricio.mycar.extensions.getLong
import br.com.gmauricio.mycar.extensions.getString
import br.com.gmauricio.mycar.models.Car
import br.com.gmauricio.mycar.models.RequestState
import br.com.gmauricio.mycar.ui.base.BaseAuthFragment

class CadastroCarroFragment : BaseAuthFragment() {

    override val layout: Int = R.layout.fragment_cadastro_carro

    private val cadastroCarroViewModel: CadastroCarroViewModel by viewModels()

    private lateinit var etMarca: EditText
    private lateinit var etModelo: EditText
    private lateinit var etAno: EditText
    private lateinit var etValor: EditText

    private lateinit var btCalculate: Button
    private lateinit var btHome: ImageView
    private lateinit var btClear: TextView

    private var editId: String? = null

    val args: CadastroCarroFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        registerObserver()
    }

    private fun setUpView(view: View) {
        etMarca = view.findViewById(R.id.etMarca)
        etModelo = view.findViewById(R.id.etModelo)
        etAno = view.findViewById(R.id.etAno)
        etValor = view.findViewById(R.id.etValor)
        btCalculate = view.findViewById(R.id.btCalculate)
        btClear = view.findViewById(R.id.btClear)
        btHome = view.findViewById(R.id.btHome)
        setUpListener()
    }

    private fun setUpListener() {
        btCalculate.setOnClickListener {
            cadastroCarroViewModel.cadastraCarro(
                editId,
                etMarca.getString(),
                etModelo.getString(),
                etAno.getLong(),
                etValor.getDouble(),
            )
        }

        btClear.setOnClickListener {
            clearFields()
        }

        btHome.setOnClickListener{
            findNavController().navigate(R.id.listCarsFragment)
        }

        args.carroId?.let {
            cadastroCarroViewModel.findToeditCar(it)
        }
    }

    private fun clearFields() {
        etMarca.setText("")
        etModelo.setText("")
        etAno.setText("")
        etValor.setText("")
    }

    private fun registerObserver() {
        cadastroCarroViewModel.carState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    hideLoading()
                    showDialogSuccess()
                }
                is RequestState.Error -> {
                    hideLoading()
                    showMessage(it.throwable.message)
                }
                is RequestState.Loading -> {
                    showLoading(getString(R.string.loading_message_processing))
                }
            }
        })

        cadastroCarroViewModel.carEditState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    loadForm(it?.data)
                    hideLoading()
                }
                is RequestState.Error -> {
                    hideLoading()
                    showMessage(it.throwable.message)
                }
                is RequestState.Loading -> {
                    showLoading(getString(R.string.loading_message_processing))
                }
            }
        })
    }

    private fun loadForm(car: Car) {
        editId = car.id
        etMarca.setText(car.marca)
        etModelo.setText(car.modelo)
        etAno.setText(car.ano.toString())
        etValor.setText(car.valor.toString())
    }

    private fun showDialogSuccess() {
        val builder = AlertDialog.Builder(context)
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.saved_sucefully))
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                findNavController().navigate(R.id.listCarsFragment)
            }
        builder.create().show()
    }


}