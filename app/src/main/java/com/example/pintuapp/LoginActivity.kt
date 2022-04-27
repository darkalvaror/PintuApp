package com.example.pintuapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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
    private var imgUrl = ""
    private var userName = ""
    private var userSurname = ""
    private var email = ""
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

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
                        showHome(it.result.user?.email.toString(), false)
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

    private fun showHome(email: String, googleLogin: Boolean) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("googleLogin", googleLogin)
            putExtra("loginSucces", true)
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

        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            binding.progressBar.visibility = View.VISIBLE

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                userName = account.givenName.toString()
                                userSurname = account.familyName.toString()
                                email = account.email.toString()
                                imgUrl = account.photoUrl.toString()

                                db.collection("Usuario").document(email).get().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val document = task.result
                                        if (document != null) {
                                            if (!document.exists()) {
                                                db.collection("Usuario").document(email).set(
                                                    hashMapOf("Nombre" to userName,
                                                    "Apellidos" to userSurname,
                                                    "Email" to email,
                                                    "Img_url" to imgUrl)
                                                )
                                                showAlert(getString(R.string.creating_user))

                                                handler = Handler(Looper.myLooper()!!)
                                                handler.postDelayed({
                                                    showHome(email, true)
                                                }, 600)
                                            } else {
                                                showHome(email, true)
                                            }
                                        }
                                    }
                                }


                            } else {
                                showAlert(getString(R.string.errorLogin))
                            }
                        }
                }
            } catch (e: ApiException) {
                showAlert(e.toString())
            }

        }
    }
}