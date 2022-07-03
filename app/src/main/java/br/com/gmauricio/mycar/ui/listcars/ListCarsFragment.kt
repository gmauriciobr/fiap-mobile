package br.com.gmauricio.mycar.ui.listcars

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.gmauricio.mycar.R
import br.com.gmauricio.mycar.models.Car
import br.com.gmauricio.mycar.models.RequestState
import br.com.gmauricio.mycar.ui.base.BaseAuthFragment
import br.com.gmauricio.mycar.ui.listcars.adapter.ListCarsAdapter

class ListCarsFragment : BaseAuthFragment() {

    override val layout: Int = R.layout.fragment_list_cars

    private val listCarsViewModel: ListCarsViewModel by viewModels()

    private lateinit var btNovo: Button

    private lateinit var rcListCar: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        registerObserver()
    }

    @SuppressLint("ResourceType")
    private fun setUpView(view: View) {
        btNovo = view.findViewById(R.id.btNovo)
        rcListCar = view.findViewById(R.id.rcListCar)
        rcListCar.layoutManager = LinearLayoutManager(context)
        setUpListener()

    }

    private fun setUpListener() {
        listCarsViewModel.findAll()

        btNovo.setOnClickListener {
            findNavController().navigate(R.id.betterFuelFragment)
        }
    }

    private fun registerObserver() {
        listCarsViewModel.carState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    uploadRecycleView(it?.data)
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

        listCarsViewModel.deleteState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    showMessage(getString(R.string.delete_succefuly))
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

    private fun uploadRecycleView(cars: List<Car>) {
        cars.forEach(::println)
        var myAdapter = ListCarsAdapter(cars)
        rcListCar.adapter = myAdapter
        myAdapter.onItemClickListener = { select ->
            select.id?.let { showDialogMenu(select) }
        }
    }

    private fun showDialogMenu(car: Car) {
        println("click carro: ${car}")
        val bundle = Bundle()
        bundle.putString("carroId", car.id)
        val builder = AlertDialog.Builder(context)
        val option = arrayOf(getString(R.string.edit), getString(R.string.delete))
        builder.setItems(option) { dialog, which ->
            when (which) {
                0 -> findNavController().navigate(R.id.betterFuelFragment, bundle)
                1 -> showDialogDel(car.id ?: "")
            }
        }
        builder.create().show()
    }

    private fun showDialogDel(id: String) {
        val builder = AlertDialog.Builder(context)
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.want_to_delete_menssage))
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                listCarsViewModel.deleteById(id)
            }
            .setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

}