package com.example.pintuapp.domain.repository

import androidx.appcompat.app.AppCompatActivity
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepo: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val categoryList = mutableListOf<OffersDataClass>()
}