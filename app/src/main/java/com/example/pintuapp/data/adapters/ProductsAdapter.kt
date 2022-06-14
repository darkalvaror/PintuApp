package com.example.pintuapp.data.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.BuildConfig
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.AddProductFragment
import com.example.pintuapp.presentation.fragments.ProductsDetailBottomSheet
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProductsAdapter(private val parentActivity: MainActivity, private var productList: MutableList<ProductsDataClass>): RecyclerView.Adapter<ProductsAdapter.CustomViewHolder>(), View.OnCreateContextMenuListener {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_highlights_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val prefs = parentActivity.getSharedPreferences(parentActivity.getString(R.string.prefs_file), Context.MODE_PRIVATE)
        var activatedFavourite = false
        val products = productList[position]
        val view = holder.itemView
        holder.itemView.context

        if (BuildConfig.adminMode) {
            parentActivity.registerForContextMenu(view)
            parentActivity.setCollectionName("Productos")
        }

        val name = view.findViewById<TextView>(R.id.productName)
        val price = view.findViewById<TextView>(R.id.price)
        val img = view.findViewById<ImageView>(R.id.productImg)
        val background = view.findViewById<ConstraintLayout>(R.id.itemBackground)
        val favouriteButton = view.findViewById<ImageButton>(R.id.imageButton2)

        background.setOnLongClickListener {
            if (BuildConfig.adminMode) {
                parentActivity.setProduct(products)
            }
            return@setOnLongClickListener false
        }

        if (BuildConfig.adminMode) {
            background.setOnCreateContextMenuListener(this)
        }

        name.text = products.Nombre
        price.text = products.Precio.toString() + "€"
        if (products.Img.isNotBlank()) {
            Picasso.get().load(products.Img).into(img)
        }
        if (products.Background.isNotBlank()) {
            background.setBackgroundColor(Color.parseColor(products.Background))
        } else {
            background.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        if (prefs.getString("email", null) != null) {
            db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val product = document.toObject(ProductsDataClass::class.java)
                    if (product.Nombre == products.Nombre) {
                        favouriteButton.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_favorite_24))
                        activatedFavourite = true
                    }
                }
            }
        }

        if (products.Nombre == parentActivity.getString(R.string.add)) {
            name.textSize = 16F
            price.text = ""
            favouriteButton.visibility = View.GONE
        }

        favouriteButton.setOnClickListener {
            if (activatedFavourite) {
                activatedFavourite = false
                favouriteButton.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_favorite_border_24))
                db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").document(products.Nombre).delete()
            } else {
                if (prefs.getString("email", null) != null) {
                    activatedFavourite = true
                    favouriteButton.setImageDrawable(ContextCompat.getDrawable(parentActivity, R.drawable.ic_baseline_favorite_24))
                    db.collection("Usuario").document(prefs.getString("email", null)!!).collection("Favoritos").document(products.Nombre).set(
                        hashMapOf("Nombre" to products.Nombre,
                            "Descripcion" to products.Descripcion,
                            "Img" to products.Img,
                            "Precio" to products.Precio)
                    )
                } else {
                    Toast.makeText(parentActivity.applicationContext, parentActivity.getString(R.string.need_login), Toast.LENGTH_SHORT).show()
                }
            }
        }



        view.setOnClickListener {
            if (products.Nombre == parentActivity.getString(R.string.add)) {
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, AddProductFragment(parentActivity))
                    commit()
                }
            } else {
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, ProductsDetailBottomSheet(products))
                    commit()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onCreateContextMenu(
        p0: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        p0?.add(0, 120, 0, parentActivity.getString(R.string.edit))
        p0?.add(0, 121, 1, parentActivity.getString(R.string.delete))
    }
}