package com.example.pintuapp.presentation.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.CheckOrdersDetailAdapter
import com.example.pintuapp.data.dataClass.CheckOrdersDataClass
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.data.dataClass.UserDataClass
import com.example.pintuapp.databinding.FragmentCheckOrdersDetailBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class CheckOrdersDetailFragment(val order: CheckOrdersDataClass) : Fragment() {

    private lateinit var binding: FragmentCheckOrdersDetailBinding
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckOrdersDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(
            requireActivity().getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )

        binding.apply {
            emailText.text = order.email
            stateText.text = order.estado
            Picasso.get().load(order.img).into(userImg2)

            if (stateText.text == "Esperando aprobación") {
                stateText.setTextColor(Color.YELLOW)
            } else if (stateText.text == "Denegado") {
                stateText.setTextColor(Color.RED)
            } else {
                stateText.setTextColor(Color.GREEN)
            }

            db.collection("Usuario").document(emailText.text.toString()).get().addOnSuccessListener { document ->
                val user = document.toObject(UserDataClass::class.java)
                if (user != null) {
                    phoneText.text = user.Telefono
                } else {
                    phoneText.text = "No phone"
                }
                streetText.text = user!!.Direccion
            }

            recyclerView.adapter = CheckOrdersDetailAdapter(activity as MainActivity, order.products as MutableList<ProductsDataClass>)
            recyclerView.layoutManager = GridLayoutManager(context, 1)

            backButton7.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, CheckOrdersFragment())
                    commit()
                }
            }

            acceptButton.setOnClickListener {
                db.collection("Usuario").document(order.email).collection("Pedidos").document(order.id).update("estado", "Aceptado")
                db.collection("Pedidos").document(order.id).update("estado", "Aceptado")
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, CheckOrdersFragment())
                    commit()
                }
            }

            declineButton.setOnClickListener {
                db.collection("Usuario").document(order.email).collection("Pedidos").document(order.id).update("estado", "Denegado")
                db.collection("Pedidos").document(order.id).update("estado", "Denegado")
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, CheckOrdersFragment())
                    commit()
                }
            }
        }
    }
}