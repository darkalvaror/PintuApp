package com.example.pintuapp.data.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.CheckOrdersDataClass
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.AddCategoryFragment
import com.example.pintuapp.presentation.fragments.CheckOrdersDetailFragment
import com.squareup.picasso.Picasso

class CheckOrdersAdapter(private var parentActivity: MainActivity, private var orderList: MutableList<CheckOrdersDataClass>): RecyclerView.Adapter<CheckOrdersAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.check_orders_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val products = orderList[position]
        val view = holder.itemView
        holder.itemView.context

        val productList = view.findViewById<TextView>(R.id.productList)
        val email = view.findViewById<TextView>(R.id.emailText)
        val state = view.findViewById<TextView>(R.id.stateText)
        val price = view.findViewById<TextView>(R.id.priceText)
        val userImg = view.findViewById<ImageView>(R.id.userImg)
        val productImg = view.findViewById<ImageView>(R.id.productImg)
        var totalPrice = 0L
        val productMutableList = mutableListOf<String>()

        productList.text = products.toString()
        email.text = products.email
        state.text = products.estado
        if (!products.img.isNullOrEmpty()) {
            Picasso.get().load(products.img).into(userImg)
        }
        Picasso.get().load(products.products[0].Img).into(productImg)

        for (product in products.products) {
            totalPrice += product.Precio!!
            productMutableList.add(product.Nombre)
        }

        productList.text = productMutableList.toString()
        price.text = totalPrice.toString() + "€"

        if (state.text == "Esperando aprobación") {
            state.setTextColor(Color.YELLOW)
        } else if(state.text == "Denegado") {
            state.setTextColor(Color.RED)
        } else {
            state.setTextColor(Color.GREEN)
        }

        view.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, CheckOrdersDetailFragment(products))
                commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}