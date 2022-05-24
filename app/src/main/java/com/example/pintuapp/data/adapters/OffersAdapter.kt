package com.example.pintuapp.data.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.example.pintuapp.R
import com.example.pintuapp.databinding.OffersItemLayoutBinding
import com.squareup.picasso.Picasso

class OffersAdapter(private val offerList: List<OffersDataClass>):RecyclerView.Adapter<OffersAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.offers_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val offer = offerList[position]
        val view = holder.itemView
        holder.itemView.context

        val text = view.findViewById<TextView>(R.id.textOffers)
        val image = view.findViewById<ImageView>(R.id.imageView6)
        val background = view.findViewById<ImageView>(R.id.image)

        text.text = offer.Nombre
        Picasso.get().load(offer.Img).into(image)
        if (offer.Background.isEmpty()) {
            background.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            background.setBackgroundColor(Color.parseColor(offer.Background))
        }

    }

    override fun getItemCount(): Int {
        return offerList.size
    }


}