package com.example.pintuapp.presentation.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.CartAdapter
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.databinding.FragmentCartBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val db = FirebaseFirestore.getInstance()
    private var finalPrice = "0"

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

        val prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)

        Checkout.preload(requireActivity().applicationContext)

        binding.googlePayButton.setOnClickListener {
            startPayment()
        }

        var quantity = 0L
        var price = 0L
        var total = 0L

        db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Carrito").get()
            .addOnSuccessListener { documents ->
                val productsList = mutableListOf<ProductsDataClass>()

                for (document in documents) {
                    val product = document.toObject(ProductsDataClass::class.java)
                    productsList.add(product)
                }

                binding.apply {
                    if (activity != null) {
                        recyclerView.adapter = CartAdapter(activity as MainActivity, productsList)
                        recyclerView.layoutManager = GridLayoutManager(context, 1)
                    }

                    binding.subtotalPrice.text = total.toString() + "€"
                    if (binding.subtotalPrice.text.toString() == "0€") {
                        binding.totalPrice.text = (total).toString() + "€"
                    } else {
                        binding.totalPrice.text = (total + 2).toString() + "€"
                    }
                    finalPrice = (total + 2).toString()
                }
            }

        db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Carrito")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Carrito").get()
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
                        if (binding.subtotalPrice.text.toString() == "0€") {
                            binding.totalPrice.text = (total).toString() + "€"
                        } else {
                            binding.totalPrice.text = (total + 2).toString() + "€"
                        }
                        finalPrice = (total + 2).toString()

                        if (activity != null) {
                            binding.recyclerView.adapter = CartAdapter(activity as MainActivity, newList)
                            binding.recyclerView.layoutManager = GridLayoutManager(context, 1)
                        }
                    }
            }
    }

    private fun startPayment() {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_uhp4xCx7F3e5ff")
        val price = finalPrice + "00"
        if (((finalPrice.toInt() - 2) == 0) || (finalPrice == "0") || (finalPrice.toInt() == 0)) {
            Toast.makeText(context, "Add something to the basket", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val options = JSONObject()
                options.put("name", "PintuApp")
                options.put("description", "Pedido de ejemplo")
                options.put("image", "https://i.ibb.co/1mbfNwr/logo.png")
                options.put("theme.color", "#3399cc")
                options.put("currency", "EUR")
                options.put("amount", price)
                val retryObj = JSONObject()
                retryObj.put("enabled", true)
                retryObj.put("max_count", 4)
                options.put("retry", retryObj)

                checkout.open(activity as MainActivity, options)
            } catch (e: Exception) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e)
            }
        }
    }
}