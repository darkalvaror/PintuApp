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
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.example.pintuapp.databinding.FragmentAddOfferBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.fragment_add_product.*

class AddOfferFragment(private val parentActivity: MainActivity, private val offer: OffersDataClass? = null) : Fragment() {

    private lateinit var binding : FragmentAddOfferBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOfferBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOfferOnDB()

        setBackgroundColor()

        setText()

        binding.backButton5.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, HomeFragment())
                commit()
            }
        }
    }

    private fun setOfferOnDB() {
        binding.button3.setOnClickListener {
            if (binding.textOfferName.text!!.isEmpty() || binding.textBackground.text!!.isEmpty() || binding.textDescription.text!!.isEmpty() || binding.textImgUrl.text!!.isEmpty()) {
                Toast.makeText(parentActivity, getString(R.string.completeAllTheFields), Toast.LENGTH_SHORT).show()
            } else {
                db.collection("Ofertas").document(binding.textOfferName.text.toString()).set(
                    hashMapOf(
                        "Background" to binding.textBackground.text.toString(),
                        "Descripcion" to binding.textDescription.text.toString(),
                        "Img" to binding.textImgUrl.text.toString(),
                        "Nombre" to binding.textOfferName.text.toString()
                    )
                )
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, HomeFragment())
                    commit()
                }
            }
        }
    }

    private fun setBackgroundColor() {
        var num = 1
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

            if (offer != null) {
                if (num == 1) {
                    binding.textBackground.setText(offer.Background)
                    binding.colorState.setBackgroundColor(envelope.color)
                    num = 0
                }
            }

            binding.okayText.setOnClickListener {
                binding.colorPickerView.visibility = View.GONE
            }
        })
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun setText() {
        if (offer != null) {
            binding.apply {
                textOfferName.setText(offer.Nombre)
                textOfferName.isEnabled = false
                textImgUrl.setText(offer.Img)
                textBackground.setText(offer.Background)
                textDescription.setText(offer.Descripcion)
                button3.text = parentActivity.getString(R.string.edit)
            }
        }
    }
}