package com.example.pintuapp

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepo {

    private val db = FirebaseFirestore.getInstance()
    private val categoryList = mutableListOf<CategoryDataClass>()

    fun getCategoryData(): MutableList<CategoryDataClass> {

        db.collection("Categoria").get().addOnSuccessListener { result ->
            for (category in result) {
                val categoryObject = category.toObject(CategoryDataClass::class.java)
                categoryList.add(categoryObject)
            }
            Log.d("Categoria: ", "$categoryList")
        }
        return categoryList
    }
}