package com.example.pintuapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import com.example.pintuapp.MainActivity
import com.example.pintuapp.R
import com.example.pintuapp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup
        setup()

        binding.imageButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setup() {
        binding.registerButton.setOnClickListener {
            if (binding.checkBox.isChecked) {
                if (binding.textInputEdit.text!!.isEmpty() || binding.textInputEdit2.text!!.isEmpty() || binding.textInputEdit3.text!!.isEmpty() || binding.textInputEdit4.text!!.isEmpty() || binding.textInputEdit6.text!!.isEmpty()) {
                    showAlert("Complete all the fields")
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.textInputEdit3.text.toString(),
                        binding.textInputEdit4.text.toString()
                    ).addOnCompleteListener {
                        if(it.isSuccessful) {
                            showHome(it.result.user?.email.toString(), false)
                        } else {
                            showAlert("Error creating the user")
                        }
                    }
                }
            } else {
                showAlert("@string/accept_error")
            }
        }
    }

    private fun showAlert(msgError: String) {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Error")
        builder.setMessage(msgError)
        builder.setPositiveButton("Done", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showHome(email:String, googleLogin: Boolean) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, HomeFragment())
            .addToBackStack(null)
            .commit()
    }
}