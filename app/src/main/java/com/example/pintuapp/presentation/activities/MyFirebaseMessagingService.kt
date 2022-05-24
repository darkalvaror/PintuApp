package com.example.pintuapp.presentation.activities

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    private val db = FirebaseFirestore.getInstance()

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