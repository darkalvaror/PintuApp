package com.example.pintuapp.presentation.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.R
import com.example.pintuapp.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val db = FirebaseFirestore.getInstance()
    private var email: String? = null
    private var photoUrl: String? = ""
    private lateinit var handler: Handler

    private fun onBackPressed() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs =
            activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        email = prefs?.getString("email", null)

        binding.deleteButton.setOnClickListener {
            db.collection("Usuario").document(email.toString()).delete()

            handler = Handler(Looper.myLooper()!!)
            handler.postDelayed({
                val user = FirebaseAuth.getInstance().currentUser
                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User account deleted.")
                    }
                }

                prefs!!.edit().clear().apply()

                FirebaseAuth.getInstance().signOut()
                onBackPressed()
            }, 1000)
        }

        db.collection("Usuario").document(email.toString()).get().addOnSuccessListener {
            binding.nameText.setText(it.get("Nombre") as String?)
            binding.surnameText.setText(it.get("Apellidos") as String?)
            binding.emailText.setText(it.get("Email") as String?)
            if (!(it.get("Telefono") as String?).isNullOrEmpty()) {
                binding.phoneText.setText(it.get("Telefono") as String?)
            }
            if (!(it.get("Tarjeta") as String?).isNullOrEmpty()) {
                binding.creditCardNumber.setText(it.get("Tarjeta") as String)
            }
            photoUrl = it.get("Img_url") as String?
        }

        binding.saveButton.setOnClickListener {
            if ((binding.phoneText.text).isNullOrEmpty()) {
                binding.phoneText.setText("")
            }
            if ((binding.creditCardNumber.text).isNullOrEmpty()) {
                binding.creditCardNumber.setText("")
            }

            if (binding.nameText.text.toString().isEmpty()
                || binding.surnameText.text.toString().isEmpty()
                || binding.emailText.text.toString().isEmpty()
            ) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                db.collection("Usuario").document(email.toString()).set(
                    hashMapOf(
                        "Nombre" to binding.nameText.text.toString(),
                        "Apellidos" to binding.surnameText.text.toString(),
                        "Email" to binding.emailText.text.toString(),
                        "Telefono" to binding.phoneText.text.toString(),
                        "Tarjeta" to binding.creditCardNumber.text.toString(),
                        "Img_url" to photoUrl.toString()
                    )
                )
            }
        }
    }
}