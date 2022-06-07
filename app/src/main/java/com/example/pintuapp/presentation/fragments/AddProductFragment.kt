package com.example.pintuapp.presentation.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.CategoryDataClass
import com.example.pintuapp.databinding.FragmentAddProductBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.util.*


class AddProductFragment(private val parentActivity: MainActivity): Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private val db = FirebaseFirestore.getInstance()
    private var category = ""
    private var promotion = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackgroundColor()

        addProductToDB()

        spinner()

        binding.backButton3.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, HomeFragment())
                commit()
            }
        }
    }

    private fun setBackgroundColor() {

        if (isDarkModeOn()) {
            binding.colorState2.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_colorize_24))
        } else {
            binding.colorState2.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_colorize_24_dark))
        }

        colorPickerView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
            binding.textBackground.setText("#" + envelope.hexCode)
            binding.colorState.setBackgroundColor(envelope.color)


            binding.okayText.setOnClickListener {
                binding.colorPickerView.visibility = View.GONE
            }
        })

        binding.colorState.setOnClickListener {
            binding.colorPickerView.visibility = View.VISIBLE
        }
    }

    private fun addProductToDB() {

        binding.checkBox2.setOnClickListener {
            promotion = !promotion
        }

        binding.addProductButton.setOnClickListener {
            if (binding.textProductName.text!!.isEmpty()  || binding.textBackground.text!!.isEmpty() ||  binding.textDescription.text!!.isEmpty() || binding.textImgUrl.text!!.isEmpty() || binding.textPrice.text!!.isEmpty() || category == "Select a category") {
                Toast.makeText(context, getString(R.string.completeAllTheFields), Toast.LENGTH_SHORT).show()
            } else {
                db.collection("Productos").document(binding.textProductName.text.toString()).set(
                    hashMapOf("Background" to textBackground.text.toString(),
                        "Categoria" to category,
                        "Descripcion" to textDescription.text.toString(),
                        "Img" to textImgUrl.text.toString(),
                        "Nombre" to textProductName.text.toString(),
                        "Precio" to textPrice.text.toString().toInt(),
                        "Promocion" to promotion)
                )
                if (promotion) {
                    parentActivity.supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_container, HomeFragment())
                        commit()
                    }
                } else {
                    parentActivity.supportFragmentManager.beginTransaction().apply {
                        replace(R.id.frame_container, ProductsFragment())
                        commit()
                    }
                }
            }
        }
    }

    private fun spinner() {

        db.collection("Categoria").get().addOnSuccessListener { documents ->
            val customList = mutableListOf("Select a category")
            for (document in documents) {
                val categoryObject = document.toObject(CategoryDataClass::class.java)
                customList.add(categoryObject.Nombre)
            }
            val adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, customList)
            categorySpinner.adapter = adapter
        }

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                category = p0?.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}