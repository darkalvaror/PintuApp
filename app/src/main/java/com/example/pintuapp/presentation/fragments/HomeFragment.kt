package com.example.pintuapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintuapp.data.adapters.CategoryAdapter
import com.example.pintuapp.databinding.FragmentHomeBinding
import com.example.pintuapp.data.adapters.OffersAdapter
import com.example.pintuapp.data.dataClass.CategoryDataClass
import com.example.pintuapp.data.dataClass.OffersDataClass
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

        db.collection("Ofertas").get().addOnSuccessListener { documents ->
            val offerList = mutableListOf<OffersDataClass>()
            for (document in documents) {
                val categoryObject = document.toObject(OffersDataClass::class.java)
                offerList.add(categoryObject)
                binding.categoryReciclerView.adapter = OffersAdapter(offerList)
                binding.categoryReciclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.progressBar2.visibility = View.GONE
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }

        db.collection("Productos").get().addOnSuccessListener { documents ->
            val categoryList = mutableListOf<CategoryDataClass>()
            for (document in documents) {
                categoryList.add(CategoryDataClass(document.data["Nombre"].toString(), document.data["Img"].toString(), document.data["Background"].toString(), document.data["Precio"].toString()))
                binding.productRecyclerView.adapter = CategoryAdapter(categoryList)
                binding.productRecyclerView.layoutManager = GridLayoutManager(context, 3)
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
        }
    }
}