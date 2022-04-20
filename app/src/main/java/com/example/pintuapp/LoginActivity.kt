package com.example.pintuapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.pintuapp.databinding.ActivityLoginBinding
import com.example.pintuapp.fragments.HomeFragment
import com.example.pintuapp.fragments.RegisterFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup
        setup()

        binding.createUserButton.setOnClickListener {
            makeCurrentFragment(RegisterFragment())
        }
    }

    private fun makeCurrentFragment(fragment : Fragment) = supportFragmentManager.beginTransaction().apply {
            replace(R.id.registerFrameLayout, fragment)
            commit()
    }

    private fun setup() {
        binding.signInButton.setOnClickListener {
            if (binding.textInputEdit.text!!.isEmpty() || binding.textInputEdit2.text!!.isEmpty()) {
                showAlert("Complete todos los campos")
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.textInputEdit.text.toString(),
                    binding.textInputEdit2.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result.user?.email.toString(), false)
                    } else {
                        showAlert("Error al iniciar sesión")
                    }
                }
            }
        }
    }

    private fun showAlert(msgError: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msgError)
        builder.setPositiveButton("Done", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, googleLogin: Boolean) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("googleLogin", googleLogin)
            putExtra("loginSucces", true)
        }
        startActivity(intent)
        finish()
    }
}