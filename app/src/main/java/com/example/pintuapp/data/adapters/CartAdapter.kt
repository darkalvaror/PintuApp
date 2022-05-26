package com.example.pintuapp.data.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.ProductsDetailBottomSheet
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class CartAdapter(private val parentActivity: MainActivity, private var productList: MutableList<ProductsDataClass>): RecyclerView.Adapter<CartAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private var favouriteActivated: Boolean = false
    private val db = FirebaseFirestore.getInstance()
    private val prefs = parentActivity.getSharedPreferences(parentActivity.getString(R.string.prefs_file), Context.MODE_PRIVATE)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_product_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val products = productList[position]
        val view = holder.itemView
        holder.itemView.context

        val name = view.findViewById<TextView>(R.id.productName)
        val description = view.findViewById<TextView>(R.id.description)
        val price = view.findViewById<TextView>(R.id.finalPrice)
        val img = view.findViewById<ImageView>(R.id.productImg)
        val quantity = view.findViewById<TextView>(R.id.quantityText)
        val favouriteButton = view.findViewById<ImageButton>(R.id.favouriteButton)
        val deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)

        name.text = products.Nombre
        description.text = products.Descripcion
        price.text = products.Precio.toString() + "€"
        quantity.text = products.Cantidad.toString()
        Picasso.get().load(products.Img).into(img)

        db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val product = document.toObject(ProductsDataClass::class.java)
                if (product.Nombre == name.text) {
                    favouriteButton.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_favorite_24))
                    favouriteActivated = true
                }
            }
        }

        favouriteButton.setOnClickListener {
            if (favouriteActivated) {
                favouriteButton.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_favorite_border_24))
                favouriteActivated = false
                db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").document(products.Nombre).delete()
            } else {
                favouriteButton.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_favorite_24))
                favouriteActivated = true
                db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").document(products.Nombre).set(
                    hashMapOf("Nombre" to products.Nombre,
                    "Img" to products.Img,
                    "Descripcion" to products.Descripcion,
                    "Precio" to products.Precio,
                    "Cantidad" to products.Cantidad)
                )
            }
        }

        deleteButton.setOnClickListener {
            db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Carrito").document(products.Nombre).delete()
        }

        view.setOnClickListener {
            ProductsDetailBottomSheet(products).apply {
                show(parentActivity.supportFragmentManager, "ProductsDetailBottomSheet")
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }


}