package com.example.pintuapp.data.dataClass

data class OrderDataClass(
    val id: String = "",
    val products: MutableList<ProductsDataClass> = mutableListOf(),
    val estado: String = "")