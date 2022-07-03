package br.com.gmauricio.mycar.ui.listcars.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.gmauricio.mycar.R
import br.com.gmauricio.mycar.models.Car

class ListCarsAdapter(
    var cars : List<Car> = listOf(),
    var onItemClickListener: (car: Car) -> Unit = {}
) : RecyclerView.Adapter<ListCarsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.fragment_list_cars_item,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(cars[position])
    }

    override fun getItemCount() = cars.size

    inner class ViewHolder(itemView: View) :

        RecyclerView.ViewHolder(itemView) {

        private lateinit var car: Car
        var tvMarca : TextView = itemView.findViewById(R.id.tvMarca)
        var tvModelo : TextView = itemView.findViewById(R.id.tvModelo)
        var tvAno : TextView = itemView.findViewById(R.id.tvAno)
        var tvValor : TextView = itemView.findViewById(R.id.tvValor)

        init {
            itemView.setOnClickListener {
                if (::car.isInitialized) {
                    onItemClickListener(car)
                }
            }
        }

        fun bindView(car: Car) {
            this.car = car
            tvMarca.text = "Marca: ${car.marca}"
            tvModelo.text = "Modelo: ${car.modelo}"
            tvAno.text = "Ano: ${car.ano}"
            tvValor.text = "Valor: R$ ${car.valor}"
        }

    }

}