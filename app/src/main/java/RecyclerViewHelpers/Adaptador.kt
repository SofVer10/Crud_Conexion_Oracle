package RecyclerViewHelpers

import Modelos.Conexion
import Modelos.ListaProductos
import android.app.AlertDialog
import android.net.Uri.Builder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sofia.palacios.ccrudsofia1.R
import java.util.UUID

class Adaptador(private var Datos: List<ListaProductos>): RecyclerView.Adapter<ViewHolder>() {

    fun actualizarRecyclerView(nuevaLista: List<ListaProductos>){
        Datos = nuevaLista
        notifyDataSetChanged() //Notifica que hay datos nuevos
    }




    //1. Crear la función de eliminar
    fun eliminarRegistro(nombreProducto: String, posicion: Int) {
        //Notificar al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        //GlobalScope abre la corrutina
        //launch lanza la corrutina
        //Dispatchers para elegir el tipo de corrutina
        //Quitar de la base de datos
        GlobalScope.launch (Dispatchers.IO){
            //Dos pasos para eliminar de la base de datos

            //1. Crear un objeto de la clase conexión
            val objConexion = Conexion().cadenaConexion()

            //2. Creo una variable que contenga un PrepareStatement
            val deleteProducto = objConexion?.prepareStatement("delete tbProductosA1 where nombreProducto =?")!!
            deleteProducto.setString(1, nombreProducto)
            deleteProducto.executeUpdate()

            val commit =objConexion.prepareStatement("commit")
            commit.executeUpdate()

            //rollback es como un control z en Oracle, permite recuperar los datos borrados
            //commit es para que se elimine los datos permanentemente

        }

        //Notificamos el cambio para que refresque la lista
        Datos = listaDatos.toList()
        //Quito los datos de la lista
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    //Actualizar datos

    fun actualizarListaDespuesdeEditar(uuid: String, nuevoNombre: String) {

        //Obtener UUID
        val index = Datos.indexOfFirst { it.uuid == uuid }
        //Asigno el nuevo Nombre
        Datos[index].nombreProducto = nuevoNombre
        //Notifico que los cambios han sido realizados
        notifyItemChanged(index)
    }

    //Creamos la función de editar o actualizar en la base de datos
    fun editarProducto (nombreProducto: String, uuid: String){
        //Creamos una corrutina
        GlobalScope.launch(Dispatchers.IO){
            //1.Creo un objeto de la clase conxión
            val objConexion = Conexion().cadenaConexion()

            //2. Creo una variable que contenga un PrepareStament
            val updateProducto = objConexion?.prepareStatement("update tbProductosA1 set nombreProducto = ? where UUID = ?")!!
            updateProducto.setString(1, nombreProducto)
            updateProducto.setString(2, uuid)
            updateProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")

            commit.executeUpdate()
        }

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

        //Darle clic al ícono de borrar
        holder.imgBorrar.setOnClickListener {

            //Crear una aletra de confirmación para que se borre
            val context = holder.itemView.context

            //builder es el que crea el cuadro de la alerta
            val builder = AlertDialog.Builder(context)

            builder.setTitle("ELIMINAR")
            builder.setMessage("Estas seguro que deseas eliminar")

            //Si el resultado es positivo
            //botones de mi alerta
            builder.setPositiveButton("Si"){
                                           //Lo que va despues de la flecha es lo que se va a ejecutar se puede cambiar de pantalla también
                dialog, wich ->
                eliminarRegistro(producto.nombreProducto, position )
            }

            //Si el resultado es negativo
            builder.setNegativeButton("No"){
                dialog, wich ->
                //Si doy clic en "No" se cierra la alerta
                dialog.dismiss()
            }

            //Para mostrar la alerta
            val dialog = builder.create()
            dialog.show()
        }
    }




}