package com.example.pintuapp.data.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.data.listeners.ProductsListener
import com.squareup.picasso.Picasso

class ProductsAdapter(private var productList: MutableList<ProductsDataClass>): RecyclerView.Adapter<ProductsAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_highlights_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val products = productList.get(position)
        val view = holder.itemView
        holder.itemView.context

        val name = view.findViewById<TextView>(R.id.productName)
        val price = view.findViewById<TextView>(R.id.price)
        val img = view.findViewById<ImageView>(R.id.productImg)
        val background = view.findViewById<ConstraintLayout>(R.id.background)

        name.text = products.Nombre
        price.text = products.Precio + " €"
        if (products.Img.isNotBlank()) {
            Picasso.get().load(products.Img).into(img)
        }
        if (products.Background.isNotBlank()) {
            background.setBackgroundColor(Color.parseColor(products.Background))
        } else {
            background.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount(): Int {
        return productList.size ?: 0
    }
}