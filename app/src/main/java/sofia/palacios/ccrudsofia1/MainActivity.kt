package sofia.palacios.ccrudsofia1

import Modelos.Conexion
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

        //2. Crear un adaptador


        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        //2. Programar el botón de agregar
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //Guardar datos
                //1.Crear un objeto de la clase conexión
                val objConexion = Conexion().cadenaConexion()

                //2. Crear una varible que sea igual a un PrepareStatement
                val addProducto = objConexion?.prepareStatement("insert into tbProductosA1 values(?, ?, ?)")!!
                addProducto.setString(1, txtNombre.text.toString())
                addProducto.setInt(2, txtPrecio.text.toString().toInt())
                addProducto.setInt(3,txtCantidad.text.toString().toInt())
                addProducto.executeUpdate()
            }
        }

        //Mostrar datos


    }
}



