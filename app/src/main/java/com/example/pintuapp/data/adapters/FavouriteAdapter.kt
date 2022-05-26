package com.example.pintuapp.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.ProductsDetailBottomSheet
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class FavouriteAdapter(private var parentActivity: MainActivity, private var favouriteList: MutableList<ProductsDataClass>): RecyclerView.Adapter<FavouriteAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private val db = FirebaseFirestore.getInstance()
    private var favouriteActivated: Boolean = false
    private val prefs = parentActivity.getSharedPreferences(parentActivity.getString(R.string.prefs_file), Context.MODE_PRIVATE)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val products = favouriteList[position]
        val view = holder.itemView
        holder.itemView.context

        val name = view.findViewById<TextView>(R.id.nameText)
        val description = view.findViewById<TextView>(R.id.favouriteDescription)
        val price = view.findViewById<TextView>(R.id.priceText)
        val img = view.findViewById<ImageView>(R.id.favouriteImg)
        val favouriteButton = view.findViewById<ImageButton>(R.id.imageView13)

        name.text = products.Nombre
        description.text = products.Descripcion
        price.text = products.Precio.toString()
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
            db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").document(products.Nombre).delete()
            favouriteActivated = false

        }

        view.setOnClickListener {
            ProductsDetailBottomSheet(products).apply {
                show(parentActivity.supportFragmentManager, "ProductsDetailBottomSheet")
            }
        }
    }

    override fun getItemCount(): Int {
        return favouriteList.size
    }
}