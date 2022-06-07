package com.example.pintuapp.data.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.CategoryDataClass
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.data.listeners.ProductsListener
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.AddCategoryFragment
import com.example.pintuapp.presentation.fragments.AddProductFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class CategoryAdapter(private var parentActivity: MainActivity, private var categoryList: List<CategoryDataClass>, val callback: ProductsListener) : RecyclerView.Adapter<CategoryAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val category = categoryList[position]
        val view = holder.itemView
        holder.itemView.context

        val background = view.findViewById<ImageView>(R.id.categoryBackground)
        val img = view.findViewById<ImageView>(R.id.categoryImg)

        if (category.Background.isNotEmpty()) {
            var buttonDrawable = background.background
            buttonDrawable = DrawableCompat.wrap(buttonDrawable)
            DrawableCompat.setTint(buttonDrawable, Color.parseColor(category.Background))
            background.background = buttonDrawable
        } else {
            background.setImageDrawable(R.id.notification.toDrawable())
        }

        if (category.Img.isNotEmpty()) {
            Picasso.get().load(category.Img).into(img)
        }

        view.setOnClickListener {

            if (category.Nombre == "Add" || category.Nombre == "Añadir") {
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, AddCategoryFragment(parentActivity))
                    commit()
                }
            } else {
                db.collection("Productos").whereEqualTo("Categoria", category.Nombre).get().addOnSuccessListener { documents ->
                    val productList = mutableListOf<ProductsDataClass>()

                    for (document in documents) {
                        val productObject = document.toObject(ProductsDataClass::class.java)
                        productList.add(productObject)
                    }
                    callback.onClickCategory(productList)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}