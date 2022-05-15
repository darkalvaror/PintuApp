package com.example.pintuapp.data.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.CategoryDataClass
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.squareup.picasso.Picasso

class CategoryAdapter(private val categoryList: List<CategoryDataClass>): RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_highlights_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val category = categoryList[position]
        val view = holder.itemView
        holder.itemView.context

        val name = view.findViewById<TextView>(R.id.productName)
        val price = view.findViewById<TextView>(R.id.price)
        val img = view.findViewById<ImageView>(R.id.productImg)
        val background = view.findViewById<ConstraintLayout>(R.id.background)

        name.text = category.Nombre
        price.setText(category.Precio + " €")
        if (category.Img.isNotBlank()) {
            Picasso.get().load(category.Img).into(img)
        }
        if (category.Background.isNotBlank()) {
            background.setBackgroundColor(Color.parseColor(category.Background))
        } else {
            background.setBackgroundColor(Color.parseColor("#C32323"))
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}