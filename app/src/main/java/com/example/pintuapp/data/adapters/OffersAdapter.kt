package com.example.pintuapp.data.adapters

import android.graphics.Color
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.BuildConfig
import com.example.pintuapp.data.dataClass.OffersDataClass
import com.example.pintuapp.R
import com.example.pintuapp.databinding.CategoryDetailBottomSheetBinding
import com.example.pintuapp.databinding.OffersItemLayoutBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.example.pintuapp.presentation.fragments.AddOfferFragment
import com.example.pintuapp.presentation.fragments.AddProductFragment
import com.example.pintuapp.presentation.fragments.CategoryDetailBottomSheet
import com.squareup.picasso.Picasso

class OffersAdapter(private val parentActivity: MainActivity, private val offerList: List<OffersDataClass>):RecyclerView.Adapter<OffersAdapter.CustomViewHolder>(), View.OnCreateContextMenuListener {

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.offers_item_layout, parent, false)
        parent.context
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val offer = offerList[position]
        val view = holder.itemView
        holder.itemView.context

        if (BuildConfig.adminMode) {
            parentActivity.registerForContextMenu(view)
            parentActivity.setCollectionName("Ofertas")
        }

        val text = view.findViewById<TextView>(R.id.textOffers)
        val image = view.findViewById<ImageView>(R.id.imageView6)
        val background = view.findViewById<ImageView>(R.id.image)
        val constraint = view.findViewById<ConstraintLayout>(R.id.constraint)

        constraint.setOnLongClickListener {
            if (BuildConfig.adminMode) {
                parentActivity.setOffer(offer)
            }
            return@setOnLongClickListener false
        }

        constraint.setOnCreateContextMenuListener(this)
        text.text = offer.Nombre
        Picasso.get().load(offer.Img).into(image)
        if (offer.Background.isEmpty()) {
            background.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            background.setBackgroundColor(Color.parseColor(offer.Background))
        }

        view.setOnClickListener {
            if (offer.Nombre == parentActivity.getString(R.string.add)) {
                parentActivity.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_container, AddOfferFragment(parentActivity))
                    commit()
                }
            } else {
                CategoryDetailBottomSheet(offer).apply {
                    show(parentActivity.supportFragmentManager, "CategoryDetailBottomSheet")
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    override fun onCreateContextMenu(
        p0: ContextMenu?,
        p1: View?,
        p2: ContextMenu.ContextMenuInfo?
    ) {
        p0?.add(0, 122, 0, parentActivity.getString(R.string.edit))
        p0?.add(0, 123, 1, parentActivity.getString(R.string.delete))
    }


}