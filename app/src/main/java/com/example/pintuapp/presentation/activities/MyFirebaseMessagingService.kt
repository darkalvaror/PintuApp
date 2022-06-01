package com.example.pintuapp.presentation.activities

import android.content.Context
import com.example.pintuapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val db = FirebaseFirestore.getInstance()
    private var prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            db.collection("Notificacion").document(remoteMessage.messageId!!).set(
                hashMapOf("Mensaje" to it.body,
                "Titulo" to it.title,
                "Img" to it.imageUrl,
                    "Background" to it.color
                )
            )
        }
    }
}