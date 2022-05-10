package com.example.pintuapp

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepo: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val categoryList = mutableListOf<CategoryDataClass>()
}