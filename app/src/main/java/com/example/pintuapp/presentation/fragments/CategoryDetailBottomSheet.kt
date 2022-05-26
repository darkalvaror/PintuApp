package com.example.pintuapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.example.pintuapp.databinding.CategoryDetailBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class CategoryDetailBottomSheet(private val offerList: OffersDataClass) : BottomSheetDialogFragment() {

    private lateinit var binding: CategoryDetailBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CategoryDetailBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            Picasso.get().load(offerList.Img).into(categoryImg)

            categoryTitle.text = offerList.Nombre

            description.text = offerList.Descripcion
        }
    }
}