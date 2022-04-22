package com.example.pintuapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pintuapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        binding.signInButton.setOnClickListener {
            if (binding.textInputEdit.text!!.isEmpty() || binding.textInputEdit2.text!!.isEmpty()) {
                showAlert(getString(R.string.completeAllTheFields))
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.textInputEdit.text.toString(),
                    binding.textInputEdit2.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result.user?.email.toString(), false, "")
                    } else {
                        showAlert(getString(R.string.errorLogin))
                    }
                }
            }
        }
        binding.createUserButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.googleButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1071137173410-fig6lv8nnivjq1qph1dgj1jimhrmhrhg.apps.googleusercontent.com")
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, 100)
        }
    }

    private fun showAlert(msgError: String) {
        Toast.makeText(this, msgError, Toast.LENGTH_SHORT).show()
    }

    private fun showHome(email: String, googleLogin: Boolean, photoUrl: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("googleLogin", googleLogin)
            putExtra("loginSucces", true)
            putExtra("photoUrl", photoUrl)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if(account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful) {
                            showHome(account.email ?: "", true, account.photoUrl.toString())
                            db.collection("Usuario").document(account.email.toString()).set(
                                hashMapOf("Nombre" to account.givenName.toString(),
                                "Apellidos" to account.familyName.toString(),
                                "Email" to account.email.toString()))
                        } else {
                            showAlert(getString(R.string.errorLogin))
                        }
                    }
                }
            } catch(e: ApiException) {
                showAlert(e.toString())
            }

        }
    }
}