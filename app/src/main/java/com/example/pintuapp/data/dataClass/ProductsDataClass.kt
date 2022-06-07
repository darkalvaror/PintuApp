package com.example.pintuapp.data.dataClass

data class ProductsDataClass(
    var Nombre: String = "",
    var Img: String = "",
    var Background: String = "",
    var Precio: Long? = 0L,
    var Categoria: String = "",
    var Promocion: Boolean = false,
    var Descripcion: String = "No info",
    var Cantidad: Long? = 1L
)