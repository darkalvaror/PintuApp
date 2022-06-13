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
import com.example.pintuapp.data.dataClass.NotificationDataClass
import com.example.pintuapp.databinding.FragmentAddNotificationBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import kotlinx.android.synthetic.main.fragment_add_product.*

class AddNotificationFragment(private val parentActivity: MainActivity, private val notification: NotificationDataClass? = null) : Fragment() {

    private lateinit var binding: FragmentAddNotificationBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNotificationToDB()

        setBackgroundColor()

        setText()

        binding.backButton4.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, NotificationFragment())
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

        var num = 1
        colorPickerView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
            binding.textBackground.setText("#" + envelope.hexCode)
            binding.colorState.setBackgroundColor(envelope.color)

            if (notification != null) {
                if (num == 1) {
                    binding.textBackground.setText(notification.Background)
                    binding.colorState.setBackgroundColor(envelope.color)
                    num = 0
                }
            }

            binding.okayText.setOnClickListener {
                binding.colorPickerView.visibility = View.GONE
            }
        })

        binding.colorState.setOnClickListener {
            binding.colorPickerView.visibility = View.VISIBLE
        }
    }

    private fun addNotificationToDB() {

        binding.button5.setOnClickListener {
            if (binding.textBackground.text!!.isEmpty() || binding.textNotificationImg.text!!.isEmpty() || binding.textNotificationName.text!!.isEmpty() || binding.textNotificationDescription.text!!.isEmpty()) {
                Toast.makeText(parentActivity, getString(R.string.completeAllTheFields), Toast.LENGTH_SHORT).show()
            } else {
                db.collection("Notificacion").document(binding.textNotificationName.text.toString()).set(
                    hashMapOf(
                        "Background" to binding.textBackground.text.toString(),
                        "Img" to binding.textNotificationImg.text.toString(),
                        "Mensaje" to binding.textNotificationDescription.text.toString(),
                        "Titulo" to binding.textNotificationName.text.toString(),
                        "ID" to binding.textNotificationName.text.toString()
                    )
                )
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, NotificationFragment())
                    commit()
                }
            }
        }
    }

    private fun isDarkModeOn(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun setText() {
        if (notification != null) {
            binding.apply {
                textNotificationName.setText(notification.Titulo)
                textNotificationName.isEnabled = false
                textNotificationDescription.setText(notification.Mensaje)
                textBackground.setText(notification.Background)
                textNotificationImg.setText(notification.Img)
                button5.text = parentActivity.getString(R.string.edit)
            }
        }
    }
}