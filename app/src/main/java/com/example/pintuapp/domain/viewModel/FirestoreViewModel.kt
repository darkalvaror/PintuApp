package com.example.pintuapp.domain.viewModel

import androidx.lifecycle.ViewModel
import com.example.pintuapp.domain.useCase.FirestoreUseCase

class FirestoreViewModel: ViewModel() {

    private val firestoreUseCase = FirestoreUseCase()

    fun getAllCategories() {
        /*return firestoreUseCase.getAllCategories()*/
    }

    fun getCategoryList() {
        /*return firestoreUseCase.getCategoryList()*/
    }
}