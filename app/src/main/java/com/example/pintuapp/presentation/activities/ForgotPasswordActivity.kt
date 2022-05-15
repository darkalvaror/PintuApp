package com.example.pintuapp.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pintuapp.R
import com.example.pintuapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private var email: String? = ""
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton2.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val bundle = intent.extras
        if (bundle != null) {
            email = bundle.getString("forgotPassword").toString()
            if (!email.isNullOrEmpty()) {
                binding.textInputEdit.setText(email)
            }
        }

        binding.resetButton.setOnClickListener {
            if (!binding.textInputEdit.text.isNullOrEmpty()) {
                email = binding.textInputEdit.text.toString()
                db.collection("Usuario").document(email!!).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        var auth = FirebaseAuth.getInstance()
                        auth.sendPasswordResetEmail(email.toString())
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        getString(R.string.email_sent),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, getString(R.string.email_no_exists), Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                    } else {
                        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.completeAllTheFields), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}