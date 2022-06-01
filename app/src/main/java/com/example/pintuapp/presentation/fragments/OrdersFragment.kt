package com.example.pintuapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.OrderAdapter
import com.example.pintuapp.data.dataClass.OrderDataClass
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.databinding.FragmentOrdersBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(
            requireActivity().getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )

        db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Pedidos").get().addOnSuccessListener { documents ->
            val listOrder = mutableListOf<OrderDataClass>()
            val listProducts = mutableListOf<ProductsDataClass>()

            for (document in documents) {
                val orderObject = document.toObject(OrderDataClass::class.java)
                listOrder.add(orderObject)
                for (product in orderObject.products) {
                    listProducts.add(product)
                }
            }
            if (activity != null) {
                binding.ordersRecyclerView.adapter = OrderAdapter(activity as MainActivity, listOrder)
                binding.ordersRecyclerView.layoutManager = GridLayoutManager(context, 1)
            }
        }
    }
}