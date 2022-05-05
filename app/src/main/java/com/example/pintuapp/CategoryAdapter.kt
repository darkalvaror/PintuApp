package com.example.pintuapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CategoryAdapter(private val categoryList: List<CategoryDataClass>):RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val category = categoryList[position]
        val view = holder.itemView
        holder.itemView.context

        val text = view.findViewById<TextView>(R.id.textOffers)
        val image = view.findViewById<ImageView>(R.id.imageView6)

        text.text = category.Nombre
        Picasso.get().load(category.Img).into(image)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }


}