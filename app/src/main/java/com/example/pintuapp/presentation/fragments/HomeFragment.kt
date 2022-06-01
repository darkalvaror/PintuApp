package com.example.pintuapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.ProductsAdapter
import com.example.pintuapp.databinding.FragmentHomeBinding
import com.example.pintuapp.data.adapters.OffersAdapter
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.example.pintuapp.presentation.activities.MainActivity
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

        val prefs = requireActivity().getSharedPreferences(
            requireActivity().getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )

        db.collection("Ofertas").get().addOnSuccessListener { documents ->
            val offerList = mutableListOf<OffersDataClass>()
            for (document in documents) {
                val offerObject = document.toObject(OffersDataClass::class.java)
                offerList.add(offerObject)
            }
            if (activity != null) {
                binding.categoryReciclerView.adapter = OffersAdapter(activity as MainActivity, offerList)
                binding.categoryReciclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            binding.progressBar2.visibility = View.GONE
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }

        db.collection("Ofertas").addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("TAG", "Listen failed.", e)
                return@addSnapshotListener
            }
            db.collection("Ofertas").get().addOnSuccessListener { documents ->
                val offerList = mutableListOf<OffersDataClass>()
                for (document in documents) {
                    val offerObject = document.toObject(OffersDataClass::class.java)
                    offerList.add(offerObject)
                }
                if (activity != null) {
                    binding.categoryReciclerView.adapter =
                        OffersAdapter(activity as MainActivity, offerList)
                    binding.categoryReciclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }

            db.collection("Productos").whereEqualTo("Promocion", true).get()
                .addOnSuccessListener { documents ->
                    val categoryList = mutableListOf<ProductsDataClass>()
                    for (document in documents) {
                        val products = document.toObject(ProductsDataClass::class.java)
                        categoryList.add(products)
                        if (activity != null) {
                            binding.productRecyclerView.adapter =
                                ProductsAdapter(activity as MainActivity, categoryList)
                            binding.productRecyclerView.layoutManager =
                                GridLayoutManager(context, 3)
                        }
                    }
                }.addOnFailureListener { exception ->
                    Log.w("Error", "Error getting documents: ", exception)
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT)
                        .show()
                }
        }

        if (prefs.getString("email", null) != null) {
            db.collection("Usuario").document(prefs.getString("email", null)!!)
                .collection("Favoritos").addSnapshotListener { value, e ->
                    db.collection("Productos").whereEqualTo("Promocion", true).get()
                        .addOnSuccessListener { documents ->
                            val categoryList = mutableListOf<ProductsDataClass>()
                            for (document in documents) {
                                val products = document.toObject(ProductsDataClass::class.java)
                                categoryList.add(products)
                            }
                            if (activity != null) {
                                binding.productRecyclerView.adapter =
                                    ProductsAdapter(activity as MainActivity, categoryList)
                                binding.productRecyclerView.layoutManager =
                                    GridLayoutManager(context, 3)
                            }
                        }
                }
        }

        db.collection("Productos").addSnapshotListener { value, error ->
            db.collection("Productos").whereEqualTo("Promocion", true).get()
                .addOnSuccessListener { documents ->
                    val categoryList = mutableListOf<ProductsDataClass>()
                    for (document in documents) {
                        val products = document.toObject(ProductsDataClass::class.java)
                        categoryList.add(products)
                    }
                    if (activity != null) {
                        binding.productRecyclerView.adapter =
                            ProductsAdapter(activity as MainActivity, categoryList)
                        binding.productRecyclerView.layoutManager =
                            GridLayoutManager(context, 3)
                    }
                }
        }
    }
}