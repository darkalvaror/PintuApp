package com.example.pintuapp.data.adapters

import android.graphics.Color
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.BuildConfig
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.NotificationDataClass
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.AddNotificationFragment
import com.example.pintuapp.presentation.fragments.AddProductFragment
import com.squareup.picasso.Picasso

class NotificationAdapter(private var parentActivity: MainActivity, private val notificationList: List<NotificationDataClass>):RecyclerView.Adapter<NotificationAdapter.CustomViewHolder>(), View.OnCreateContextMenuListener {

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

        if (BuildConfig.adminMode) {
            parentActivity.registerForContextMenu(view)
            parentActivity.setCollectionName("Notificacion")
        }

        val title = view.findViewById<TextView>(R.id.title)
        val body = view.findViewById<TextView>(R.id.body)
        val img = view.findViewById<ImageView>(R.id.imagen)
        val background = view.findViewById<ConstraintLayout>(R.id.background)

        background.setOnLongClickListener {
            if (BuildConfig.adminMode) {
                parentActivity.setNotification(notification)
            }
            return@setOnLongClickListener false
        }

        background.setOnCreateContextMenuListener(this)
        title.text = notification.Titulo
        body.text = notification.Mensaje

        if (!notification.Img.isNullOrEmpty()) {
            Picasso.get().load(notification.Img).into(img)
        }
        if (notification.Background.isNullOrEmpty()) {
            background.setBackgroundColor(Color.parseColor("#978A2A2A"))
        } else {
            background.setBackgroundColor(Color.parseColor(notification.Background))
        }

        view.setOnClickListener {
            if (notification.Titulo == parentActivity.getString(R.string.add)) {
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, AddNotificationFragment(parentActivity))
                    commit()
                }
            } else {

            }
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onCreateContextMenu(
        p0: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        p0?.add(0, 126, 0, parentActivity.getString(R.string.edit))
        p0?.add(0, 127, 1, parentActivity.getString(R.string.delete))
    }
}