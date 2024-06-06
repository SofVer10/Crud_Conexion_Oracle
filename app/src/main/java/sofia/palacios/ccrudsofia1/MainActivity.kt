package sofia.palacios.ccrudsofia1

import Modelos.Conexion
import Modelos.ListaProductos
import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1. Mandar a llamar todos los elementos de la vista
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val rcbDatos = findViewById<RecyclerView>(R.id.rcbDatos)


        //1. Ponerle un layout a mi RecyclerView
        rcbDatos.layoutManager = LinearLayoutManager(this)

        //2. Función para mostrar datos
        fun obtenerDatos(): List<ListaProductos>{
            val objConexion = Conexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbproductosa1")!!

            val listaProductos = mutableListOf<ListaProductos>()

            //Recorrer todos los datos que me trajo el select

            while (resultSet.next()){
                val uuid = resultSet.getString("UUID")
                val nombre = resultSet.getString("nombreProducto")
                val precio = resultSet.getInt("precio")
                val cantidad = resultSet.getInt("cantidad")
                val productos = ListaProductos(uuid, nombre, precio, cantidad)
                listaProductos.add(productos)
            }

            return listaProductos

            //CreateStament sirve para traer datos
            //(Select en base de datos)

            //prepareStament paramandat datos
            //(Actualizar, crear y eliminar)

        }

        //Ejecutamos la función
        CoroutineScope(Dispatchers.IO).launch {
            val ejecutarFuncion = obtenerDatos()


            withContext(Dispatchers.Main){
                //Uno el miAdaptador con el RecyclerView
                val miAdaptador = Adaptador(ejecutarFuncion)
                rcbDatos.adapter = miAdaptador
            }
        }


        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        //2. Programar el botón de agregar
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //Guardar datos
                //1.Crear un objeto de la clase conexión
                val objConexion = Conexion().cadenaConexion()

                //2. Crear una varible que sea igual a un PrepareStatement
                val addProducto = objConexion?.prepareStatement("insert into tbProductosA1 (UUID, nombreProducto, precio, cantidad)  values(?, ?, ?, ?)")!!
                //El UUID es para que un dato sea único
                addProducto.setString(1, UUID.randomUUID().toString())
                addProducto.setString(2, txtNombre.text.toString())
                addProducto.setInt(3, txtPrecio.text.toString().toInt())
                addProducto.setInt(4,txtCantidad.text.toString().toInt())

                addProducto.executeUpdate()

                val nuevosProductos = obtenerDatos()

                //Creo una corrutina que actualice el listado
                withContext(Dispatchers.Main){
                    (rcbDatos.adapter as? Adaptador)?.actualizarRecyclerView(nuevosProductos)
                }
            }
        }

        //La corrutina IO es para procesos secundarios, mientras que main es para mostrar algo en loa pagina de main (principal)

        //Mostrar datos


    }
}



