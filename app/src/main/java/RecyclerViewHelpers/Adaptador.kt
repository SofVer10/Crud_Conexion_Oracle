package RecyclerViewHelpers

import Modelos.ListaProductos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sofia.palacios.ccrudsofia1.R

class Adaptador(private var Datos: List<ListaProductos>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarRecyclerView(nuevaLista: List<ListaProductos>){
        Datos = nuevaLista
        notifyDataSetChanged() //Notifica que hay datos nuevos
    }

    //Colocar el mouse en la clase en este caso Adaptador y darle clic en implementar miembros
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreProducto

    }


}