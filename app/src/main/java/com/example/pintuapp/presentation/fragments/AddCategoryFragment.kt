package com.example.pintuapp.presentation.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.pintuapp.R
import com.example.pintuapp.databinding.FragmentAddCategoryBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.fragment_add_product.*


class AddCategoryFragment(private val parentActivity: MainActivity) : Fragment() {

    private lateinit var binding: FragmentAddCategoryBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCategoryOnDB()

        setBackgroundColor()

        binding.backButton6.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, ProductsFragment())
                commit()
            }
        }
    }

    private fun setCategoryOnDB() {
        binding.button4.setOnClickListener {

            if (binding.textProductName.text!!.isEmpty() || binding.textBackground.text!!.isEmpty() || binding.textImgUrl.text!!.isEmpty()) {
                Toast.makeText(context, getString(R.string.completeAllTheFields), Toast.LENGTH_SHORT).show()
            } else {
                db.collection("Categoria").document(binding.textProductName.text.toString()).set(
                    hashMapOf(
                        "Background" to binding.textBackground.text.toString(),
                        "Img" to binding.textImgUrl.text.toString(),
                        "Nombre" to binding.textProductName.text.toString()
                    )
                )
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, ProductsFragment())
                    commit()
                }
            }
        }
    }

    private fun setBackgroundColor() {
        colorPickerView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->

            binding.colorState.setOnClickListener {
                binding.colorPickerView.visibility = View.VISIBLE
            }

            if (isDarkModeOn()) {
                binding.colorState2.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_colorize_24))
            } else {
                binding.colorState2.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_colorize_24_dark))
            }

            binding.textBackground.setText("#" + envelope.hexCode)
            binding.colorState.setBackgroundColor(envelope.color)

            binding.okayText.setOnClickListener {
                binding.colorPickerView.visibility = View.GONE
            }
        })
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}