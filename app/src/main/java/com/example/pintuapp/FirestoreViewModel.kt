package com.example.pintuapp

import androidx.lifecycle.ViewModel

class FirestoreViewModel: ViewModel() {

    private val firestoreUseCase = FirestoreUseCase()

    fun getAllCategories() {
        /*return firestoreUseCase.getAllCategories()*/
    }

    fun getCategoryList() {
        /*return firestoreUseCase.getCategoryList()*/
    }
}