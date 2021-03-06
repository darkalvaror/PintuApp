package com.example.pintuapp.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pintuapp.R
import com.example.pintuapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup
        setup()
        binding.backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.checkBox.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.politics_layout, null)
            with(builder) {
                setTitle(getString(R.string.privacity_politics))
                setPositiveButton(getString(R.string.okay)) { dialog, which ->
                }
                setView(dialogLayout)
                show()
            }
        }
    }

    private fun setup() {
        binding.registerButton.setOnClickListener {
            if (binding.checkBox.isChecked) {
                if (binding.textInputEdit.text!!.isEmpty() || binding.textInputEdit2.text!!.isEmpty() || binding.textInputEdit3.text!!.isEmpty() || binding.textInputEdit4.text!!.isEmpty() || binding.textInputEdit6.text!!.isEmpty()) {
                    showAlert(getString(R.string.completeAllTheFields))
                } else {
                    if(binding.textInputEdit4.text.toString() == binding.textInputEdit6.text.toString()) {
                        db.collection("Usuario").document(binding.textInputEdit3.text.toString()).get()
                            .addOnSuccessListener {
                                if (it.exists()) {
                                    showAlert(getString(R.string.email_is_already_register))
                                } else {
                                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                                        binding.textInputEdit3.text.toString(),
                                        binding.textInputEdit4.text.toString()
                                    ).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            showHome(it.result.user?.email.toString())
                                            val img_url = "https://drive.google.com/file/d/1pjzdFUnQuJ1ZGRPK3z4PpTRos4GDeRzb/view?usp=sharing"
                                            db.collection("Usuario")
                                                .document(it.result.user?.email.toString()).set(
                                                    hashMapOf(
                                                        "Nombre" to binding.textInputEdit.text.toString(),
                                                        "Apellidos" to binding.textInputEdit2.text.toString(),
                                                        "Email" to binding.textInputEdit3.text.toString(),
                                                        "Img_url" to img_url
                                                    )
                                                )
                                        } else {
                                            showAlert(getString(R.string.errorCreatingUser))
                                        }
                                    }
                                }
                            }
                    } else {
                        showAlert(getString(R.string.check_password))
                    }
                }
            } else {
                showAlert(getString(R.string.accept_error))
            }
        }
    }

    private fun showAlert(msgError: String) {
        Toast.makeText(this, msgError, Toast.LENGTH_SHORT).show()
    }

    private fun showHome(email: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("loginSucces", true)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}