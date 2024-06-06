package Modelos

import java.util.UUID

//DataClass son los parametros que tienen geters y seters
data class ListaProductos(
    val uuid: String,
    var nombreProducto: String,
    var precio: Int,
    var cantidad: Int

)
