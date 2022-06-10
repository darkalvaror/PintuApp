package com.example.pintuapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pintuapp.BuildConfig
import com.example.pintuapp.R
import com.example.pintuapp.data.adapters.CategoryAdapter
import com.example.pintuapp.data.adapters.ProductsAdapter
import com.example.pintuapp.data.dataClass.CategoryDataClass
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.data.listeners.ProductsListener
import com.example.pintuapp.databinding.FragmentProductsBinding
import com.example.pintuapp.presentation.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class ProductsFragment : Fragment(), ProductsListener {

    private lateinit var binding: FragmentProductsBinding
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences(
            requireActivity().getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )

        db.collection("Productos").get().addOnSuccessListener { documents ->
            val productList = mutableListOf<ProductsDataClass>()
            if (BuildConfig.adminMode) {
                val addProduct = ProductsDataClass(prefs.getString("Add" , "Add")!!, Img ="https://cdn-icons-png.flaticon.com/512/189/189689.png", "#FFB1B1B1", null,  "", false, "", null)
                productList.add(addProduct)
            }
            for (document in documents) {
                val productObject = document.toObject(ProductsDataClass::class.java)
                productList.add(productObject)
            }
            if (activity != null) {
                binding.productsRecyclerView.adapter = ProductsAdapter(activity as MainActivity, productList)
                binding.productsRecyclerView.layoutManager = GridLayoutManager(context, 3)
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }

        db.collection("Categoria").get().addOnSuccessListener { documents ->
            val categoryList = mutableListOf<CategoryDataClass>()
            if (BuildConfig.adminMode) {
                val addCategory = CategoryDataClass(prefs.getString("Add", "Add")!!, "https://cdn-icons-png.flaticon.com/512/189/189689.png", "#FFB1B1B1")
                categoryList.add(addCategory)
            }
            for (document in documents) {
                val categoryObject = document.toObject(CategoryDataClass::class.java)
                categoryList.add(categoryObject)
            }
            if (activity != null) {
                binding.categoryRecyclerView.adapter = CategoryAdapter(activity as MainActivity, categoryList, this)
                binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }.addOnFailureListener { exception ->
            Log.w("Error", "Error getting documents: ", exception)
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }

        db.collection("Categoria").addSnapshotListener { value, error ->
            db.collection("Categoria").get().addOnSuccessListener { documents ->
                val categoryList = mutableListOf<CategoryDataClass>()
                if (BuildConfig.adminMode) {
                    val addCategory = CategoryDataClass(prefs.getString("Add", "Add")!!, "https://cdn-icons-png.flaticon.com/512/189/189689.png", "#FFB1B1B1")
                    categoryList.add(addCategory)
                }
                for (document in documents) {
                    val categoryObject = document.toObject(CategoryDataClass::class.java)
                    categoryList.add(categoryObject)
                }
                if (activity != null) {
                    binding.categoryRecyclerView.adapter = CategoryAdapter(activity as MainActivity, categoryList, this)
                    binding.categoryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }

        db.collection("Productos").addSnapshotListener { value, error ->
            db.collection("Productos").get().addOnSuccessListener { documents ->
                val productList = mutableListOf<ProductsDataClass>()
                if (BuildConfig.adminMode) {
                    val addProduct = ProductsDataClass(prefs.getString("Add" , "Add")!!, Img ="https://cdn-icons-png.flaticon.com/512/189/189689.png", "#FFB1B1B1", null,  "", false, "", null)
                    productList.add(addProduct)
                }
                for (document in documents) {
                    val productObject = document.toObject(ProductsDataClass::class.java)
                    productList.add(productObject)
                }
                if (activity != null) {
                    binding.productsRecyclerView.adapter = ProductsAdapter(activity as MainActivity, productList)
                    binding.productsRecyclerView.layoutManager = GridLayoutManager(context, 3)
                }
            }
        }

        if (prefs.getString("email", null) != null) {
            db.collection("Usuario").document(prefs.getString("email", null)!!)
                .collection("Favoritos").addSnapshotListener { value, error ->
                db.collection("Productos").get().addOnSuccessListener { documents ->
                    val productList = mutableListOf<ProductsDataClass>()
                    if (BuildConfig.adminMode) {
                        val addProduct = ProductsDataClass(prefs.getString("Add" , "Add")!!, Img ="https://cdn-icons-png.flaticon.com/512/189/189689.png", "#FFB1B1B1", null,  "", false, "", null)
                        productList.add(addProduct)
                    }
                    for (document in documents) {
                        val productObject = document.toObject(ProductsDataClass::class.java)
                        productList.add(productObject)
                    }
                    if (activity != null) {
                        binding.productsRecyclerView.adapter = ProductsAdapter(activity as MainActivity, productList)
                        binding.productsRecyclerView.layoutManager = GridLayoutManager(context, 3)
                    }
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                db.collection("Productos").whereEqualTo("Nombre", p0).get()
                    .addOnSuccessListener { documents ->
                        val productList = mutableListOf<ProductsDataClass>()
                        for (document in documents) {
                            val productObject = document.toObject(ProductsDataClass::class.java)
                            productList.add(productObject)
                        }
                        binding.productsRecyclerView.adapter = ProductsAdapter(activity as MainActivity, productList)
                        binding.productsRecyclerView.layoutManager = GridLayoutManager(context, 3)
                    }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    override fun onClickCategory(products: MutableList<ProductsDataClass>) {
        binding.productsRecyclerView.adapter = ProductsAdapter(activity as MainActivity, products)
        binding.productsRecyclerView.layoutManager = GridLayoutManager(context, 3)
    }
}