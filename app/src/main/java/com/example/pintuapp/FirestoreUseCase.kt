package com.example.pintuapp

class FirestoreUseCase {

    private val repo = FirebaseRepo()

    fun getAllCategories(): MutableList<CategoryDataClass> {
        return repo.getCategoryData()
    }
}