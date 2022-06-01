package com.example.pintuapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pintuapp.databinding.FragmentPoliticsAndPrivacityBinding


class PoliticsAndPrivacityFragment : Fragment() {

    private lateinit var binding: FragmentPoliticsAndPrivacityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPoliticsAndPrivacityBinding.inflate(layoutInflater)
        return binding.root
    }
}