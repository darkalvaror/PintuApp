package com.example.pintuapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintuapp.*
import com.example.pintuapp.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("Categoria").get().addOnSuccessListener { documents ->
            val categoryList = mutableListOf<CategoryDataClass>()
            for (document in documents) {
                val categoryObject = document.toObject(CategoryDataClass::class.java)
                categoryList.add(categoryObject)
                binding.categoryReciclerView.adapter = CategoryAdapter(categoryList)
                binding.categoryReciclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }
    }
}