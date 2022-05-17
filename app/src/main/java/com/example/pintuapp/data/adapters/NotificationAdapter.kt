package com.example.pintuapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.NotificationDataClass
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.example.pintuapp.databinding.NotificationItemLayoutBinding
import com.squareup.picasso.Picasso

class NotificationAdapter(private val notificationList: List<NotificationDataClass>):RecyclerView.Adapter<NotificationAdapter.CustomViewHolder>() {

    private lateinit var binding: NotificationItemLayoutBinding

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val notification = notificationList[position]
        val view = holder.itemView
        holder.itemView.context

        val title = view.findViewById<TextView>(R.id.title)
        val body = view.findViewById<TextView>(R.id.body)
        val img = view.findViewById<ImageView>(R.id.imagen)

        title.text = notification.Titulo
        body.text = notification.Mensaje
        if (!notification.Img.isNullOrEmpty()) {
            Picasso.get().load(notification.Img).into(img)
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}