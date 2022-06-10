package com.example.pintuapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.ProductsDetailBottomSheet
import com.squareup.picasso.Picasso

class CheckOrdersDetailAdapter(private var parentActivity: MainActivity, private var orderList: MutableList<ProductsDataClass>): RecyclerView.Adapter<CheckOrdersDetailAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CheckOrdersDetailAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.account_details_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val products = orderList[position]
        val view = holder.itemView
        holder.itemView.context

        val productName = view.findViewById<TextView>(R.id.productName)
        val description = view.findViewById<TextView>(R.id.productDescription)
        val quantity = view.findViewById<TextView>(R.id.productQuantity)
        val price = view.findViewById<TextView>(R.id.productPrice)
        val img = view.findViewById<ImageView>(R.id.productImg)

        productName.text = products.Nombre
        description.text = products.Descripcion
        quantity.text = products.Cantidad.toString()
        price.text = products.Precio.toString()
        Picasso.get().load(products.Img).into(img)

        view.setOnClickListener {
            parentActivity.supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, ProductsDetailBottomSheet(products))
                commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}