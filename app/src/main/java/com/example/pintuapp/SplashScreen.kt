package com.example.pintuapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.pintuapp.databinding.ActivitySplashScreenBinding
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.concurrent.Executor


class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var handler: Handler
    private var email: String? = null
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView2.visibility = View.GONE
        binding.imageView3.visibility = View.GONE

        handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            binding.imageView2.visibility = View.VISIBLE
            binding.imageView3.visibility = View.VISIBLE
        }, 3000)

        handler.postDelayed({
            session()
        }, 3000)

        val analytics = FirebaseAnalytics.getInstance(this@SplashScreen)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase")
        analytics.logEvent("InitScreen", bundle)
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        email = prefs.getString("email", null)
        if (email != null) {
            executor = ContextCompat.getMainExecutor(this)

            biometricPrompt = BiometricPrompt(this@SplashScreen, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    prefs.edit().clear().apply()
                    if (errString == "Cancelar") {
                        handler = Handler(Looper.myLooper()!!)
                        handler.postDelayed({
                            val intent = Intent(this@SplashScreen, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 500)
                    } else {
                        Toast.makeText(this@SplashScreen, errString, Toast.LENGTH_SHORT).show()
                        handler = Handler(Looper.myLooper()!!)
                        handler.postDelayed({
                            val intent = Intent(this@SplashScreen, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 500)
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@SplashScreen, "Vuelve a intentarlo", Toast.LENGTH_SHORT).show()
                }
            })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.biometric_auth))
                .setNegativeButtonText(getString(R.string.different_account))
                .build()

            biometricPrompt.authenticate(promptInfo)

        } else {
            handler = Handler(Looper.myLooper()!!)
            handler.postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }
}