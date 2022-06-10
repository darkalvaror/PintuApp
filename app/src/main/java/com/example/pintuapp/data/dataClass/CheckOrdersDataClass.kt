package com.example.pintuapp.data.dataClass

data class CheckOrdersDataClass(
    var email: String = "",
    var estado: String = "",
    var id: String = "",
    var img: String = "",
    var products: List<ProductsDataClass> = mutableListOf()
)