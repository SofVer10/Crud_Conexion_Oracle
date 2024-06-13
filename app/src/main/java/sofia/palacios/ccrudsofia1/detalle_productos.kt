package sofia.palacios.ccrudsofia1

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.UUID

class detalle_productos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_productos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1.Recibir valores
        val UUIDRecibido = intent.getStringExtra("UUID")
        val nombreRecibido = intent.getStringExtra("nombreProducto")
        val precioRecibido = intent.getIntExtra("precio", 0)
        val cantidadRecibido = intent.getIntExtra("cantidad", 0)

        //2.Mandar a llamar a todos los elementos de la pantalla
        val txtUuidDetalle = findViewById<TextView>(R.id.txtUuidDetalle)
        val txtNombreDetalle = findViewById<TextView>(R.id.txtNombreDetalle)
        val txtPrecioDetalle = findViewById<TextView>(R.id.txtPrecioDetalle)
        val txtCantidadDetalle = findViewById<TextView>(R.id.txtCantidadDetalle)

        //3.Asignar los valores que recibí en el paso 1
        //a los elementos que llamé en el paso 2

        txtUuidDetalle.text = UUIDRecibido
        txtNombreDetalle.text = nombreRecibido
        txtPrecioDetalle.text = precioRecibido.toString()
        txtCantidadDetalle.text = cantidadRecibido.toString()
    }
}