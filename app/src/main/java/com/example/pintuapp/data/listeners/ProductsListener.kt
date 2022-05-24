package com.example.pintuapp.data.listeners

import com.example.pintuapp.data.dataClass.ProductsDataClass

interface ProductsListener {
    fun onClickCategory(products: MutableList<ProductsDataClass>)
}