package com.example.pintuapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.NotificationAdapter
import com.example.pintuapp.data.dataClass.NotificationDataClass
import com.example.pintuapp.databinding.FragmentNotificationBinding
import com.google.firebase.firestore.FirebaseFirestore

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("Notificacion").get().addOnSuccessListener { documents ->
            val notificationList = mutableListOf<NotificationDataClass>()
            for (document in documents) {
                val notification = document.toObject(NotificationDataClass::class.java)
                notificationList.add(notification)
                binding.notificationRecyclerView.adapter = NotificationAdapter(notificationList)
                binding.notificationRecyclerView.layoutManager = GridLayoutManager(context, 1)
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }
}