package com.example.pintuapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.CheckOrdersAdapter
import com.example.pintuapp.data.dataClass.CheckOrdersDataClass
import com.example.pintuapp.databinding.FragmentCheckOrdersBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class CheckOrdersFragment : Fragment() {

    private lateinit var binding: FragmentCheckOrdersBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckOrdersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("Pedidos").get().addOnSuccessListener { documents ->
            val list = mutableListOf<CheckOrdersDataClass>()
            for (document in documents) {
                val checkObject = document.toObject(CheckOrdersDataClass::class.java)
                list.add(checkObject)
            }

            binding.recyclerView.adapter = CheckOrdersAdapter(activity as MainActivity, list)
            binding.recyclerView.layoutManager = GridLayoutManager(context, 1)
        }
    }
}