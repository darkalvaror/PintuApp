package com.example.pintuapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.CartAdapter
import com.example.pintuapp.data.adapters.ProductsAdapter
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.databinding.FragmentCartBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment(private val email: String) : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var quantity = 0L
        var price = 0L
        var total = 0L

        db.collection("Usuario").document(email).collection("Carrito").get()
            .addOnSuccessListener { documents ->
                val productsList = mutableListOf<ProductsDataClass>()

                for (document in documents) {
                    val product = document.toObject(ProductsDataClass::class.java)
                    productsList.add(product)
                }

                binding.apply {
                    recyclerView.adapter = CartAdapter(activity as MainActivity, productsList)
                    recyclerView.layoutManager = GridLayoutManager(context, 1)

                    subtotalPrice.text = total.toString() + "€"
                    totalPrice.text = (total + 2).toString() + "€"
                }
            }

        db.collection("Usuario").document(email).collection("Carrito")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                db.collection("Usuario").document(email).collection("Carrito").get()
                    .addOnSuccessListener { documents ->
                        val newList = mutableListOf<ProductsDataClass>()
                        quantity = 0L
                        price = 0L
                        total = 0L

                        for (document in documents) {
                            val product = document.toObject(ProductsDataClass::class.java)
                            newList.add(product)
                            quantity = document.data["Cantidad"] as Long
                            price = document.data["Precio"] as Long

                            total += (quantity * price)
                        }

                        binding.subtotalPrice.text = total.toString() + "€"
                        binding.totalPrice.text = (total + 2).toString() + "€"

                        if (activity != null) {
                            binding.recyclerView.adapter = CartAdapter(activity as MainActivity, newList)
                            binding.recyclerView.layoutManager = GridLayoutManager(context, 1)
                        }
                    }
            }
    }
}