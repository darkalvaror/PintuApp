package com.example.pintuapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pintuapp.CategoryAdapter
import com.example.pintuapp.FirestoreViewModel
import com.example.pintuapp.MainActivity
import com.example.pintuapp.R
import com.example.pintuapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel= FirestoreViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val call = viewModel.getAllCategories()

        binding.categoryReciclerView.adapter = CategoryAdapter(call)
        binding.categoryReciclerView.layoutManager = GridLayoutManager(context, 3)

    }
}