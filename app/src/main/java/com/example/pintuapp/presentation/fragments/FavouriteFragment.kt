package com.example.pintuapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.FavouriteAdapter
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.databinding.FragmentFavouriteBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class FavouriteFragment() : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(requireActivity().getString(R.string.prefs_file), Context.MODE_PRIVATE)

        db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").get().addOnSuccessListener { documents ->
                val list = mutableListOf<ProductsDataClass>()

                for (document in documents) {
                    val product = document.toObject(ProductsDataClass::class.java)
                    list.add(product)
                }
                Log.d("lista1", "$list")
                binding.recyclerView.adapter = FavouriteAdapter(activity as MainActivity, list)
                binding.recyclerView.layoutManager = GridLayoutManager(context, 1)
            }

        db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                db.collection("Usuario").document(prefs.getString("email", null)!!)
                    .collection("Favoritos").get().addOnSuccessListener { documents ->
                        val newList = mutableListOf<ProductsDataClass>()

                        for (document in documents) {
                            val product = document.toObject(ProductsDataClass::class.java)
                            newList.add(product)
                        }
                        Log.d("lista2", "$newList")
                        if (activity != null) {
                            binding.recyclerView.adapter =
                                FavouriteAdapter(activity as MainActivity, newList)
                            binding.recyclerView.layoutManager = GridLayoutManager(context, 1)
                        }
                    }
            }
    }
}