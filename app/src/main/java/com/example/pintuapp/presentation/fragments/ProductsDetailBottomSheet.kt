package com.example.pintuapp.presentation.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.databinding.ProductsDetailBottomSheetBinding
import com.example.pintuapp.presentation.activities.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProductsDetailBottomSheet(private val productList: ProductsDataClass) :
    BottomSheetDialogFragment() {

    private lateinit var binding: ProductsDetailBottomSheetBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProductsDetailBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var number = 1
        var activatedFavourite = false

        binding.apply {
            Picasso.get().load(productList.Img).into(image)
            productName.text = productList.Nombre
            description.text = productList.Descripcion

            lessButton.setOnClickListener {
                number -= 1
                if (number == 0) {
                    number = 99
                }
                numProducts.text = number.toString()
            }
            plusButton.setOnClickListener {
                number += 1
                if (number == 100) {
                    number = 1
                }
                numProducts.text = number.toString()
            }

            if (!productList.Stock) {
                stockText.text = getString(R.string.out_stock)
                stockText.setTextColor(Color.RED)
            }

            if (prefs?.getString("email", null) != null) {
                db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        val product = document.toObject(ProductsDataClass::class.java)
                        if (product.Nombre == productList.Nombre) {
                            activatedFavourite = true
                            imageButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24))
                        }
                    }
                }
            }

            imageButton.setOnClickListener {
                if (activatedFavourite) {
                    activatedFavourite = false
                    imageButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_border_24))
                    db.collection("Usuario").document(prefs?.getString("email", null)!!).collection("Favoritos").document(productList.Nombre).delete()
                } else {
                    if (prefs?.getString("email", null) != null) {
                        activatedFavourite = true
                        imageButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_favorite_24))
                        db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").document(productList.Nombre).set(
                            hashMapOf("Nombre" to productList.Nombre,
                            "Descripcion" to productList.Descripcion,
                            "Img" to productList.Img,
                            "Precio" to productList.Precio)
                        )
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.need_login), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            button.setOnClickListener {
                if (productList.Stock) {
                    if (prefs != null) {
                        if (prefs.getString("email", null) != null) {
                            db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Carrito").document(productList.Nombre).set(
                                hashMapOf("Nombre" to productList.Nombre,
                                    "Precio" to productList.Precio,
                                    "Img" to productList.Img,
                                    "Cantidad" to number,
                                    "Descripcion" to productList.Descripcion)
                            )
                        } else {
                            val intent = Intent(context, LoginActivity::class.java)
                            requireActivity().startActivity(intent)
                        }
                    } else {
                        val intent = Intent(context, LoginActivity::class.java)
                        requireActivity().startActivity(intent)
                    }
                } else {
                    Toast.makeText(context, getString(R.string.text_out_stock), Toast.LENGTH_SHORT).show()
                }
            }

            button2.setOnClickListener {
                if (productList.Stock) {
                    if (prefs != null) {
                        if (prefs.getString("email", null) != null) {
                            db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Carrito").document(productList.Nombre).set(
                                hashMapOf("Nombre" to productList.Nombre,
                                    "Precio" to productList.Precio,
                                    "Img" to productList.Img,
                                    "Cantidad" to number,
                                    "Descripcion" to productList.Descripcion)
                            )
                            requireActivity().supportFragmentManager.beginTransaction().apply {
                                replace(R.id.frame_container, CartFragment())
                                commit()
                            }
                        } else {
                            val intent = Intent(context, LoginActivity::class.java)
                            requireActivity().startActivity(intent)
                        }
                    } else {
                        val intent = Intent(context, LoginActivity::class.java)
                        requireActivity().startActivity(intent)
                    }
                } else {
                    Toast.makeText(context, getString(R.string.text_out_stock), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}