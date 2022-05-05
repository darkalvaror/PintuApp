package com.example.pintuapp

import androidx.lifecycle.ViewModel

class FirestoreViewModel: ViewModel() {

    private val firestoreUseCase = FirestoreUseCase()

    fun getAllCategories(): MutableList<CategoryDataClass>  {
        return firestoreUseCase.getAllCategories()
    }
}