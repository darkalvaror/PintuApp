package com.example.pintuapp.data.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.OrderDataClass
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.presentation.activities.MainActivity
import com.squareup.picasso.Picasso

class OrderAdapter(private var parentActivity: MainActivity, private var productList: MutableList<OrderDataClass>): RecyclerView.Adapter<OrderAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val products = productList[position]
        val view = holder.itemView
        holder.itemView.context

        val title = view.findViewById<TextView>(R.id.orderTitle)
        val description = view.findViewById<TextView>(R.id.orderDescription)
        val img = view.findViewById<ImageView>(R.id.orderImg)
        val price = view.findViewById<TextView>(R.id.orderPrice)
        var totalPrice = 0F
        val state = view.findViewById<TextView>(R.id.orderState)

        val list = mutableListOf<String>()
        for (product in products.products) {
            list.add(product.Nombre)
            Picasso.get().load(product.Img).into(img)
            totalPrice += product.Precio!!
        }

        description.text = list.toString()
        title.text = "ID: " + products.id
        price.text = totalPrice.toString() + " €"
        state.text = products.estado

        if (state.text == "Esperando aprobación") {
            state.setTextColor(Color.YELLOW)
        } else if(state.text == "Denegado") {
            state.setTextColor(Color.RED)
        } else {
            state.setTextColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}