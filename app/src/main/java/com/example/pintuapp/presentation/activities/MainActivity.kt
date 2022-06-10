package com.example.pintuapp.presentation.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.pintuapp.BuildConfig
import com.example.pintuapp.R
import com.example.pintuapp.data.dataClass.ProductsDataClass
import com.example.pintuapp.databinding.ActivityMainBinding
import com.example.pintuapp.databinding.HeaderLayoutBinding
import com.example.pintuapp.presentation.fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.razorpay.PaymentResultListener
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingHeader: HeaderLayoutBinding
    private var homeFragment: Fragment = HomeFragment()
    private var notificationFragment: Fragment = NotificationFragment()
    private var productsFragment: Fragment = ProductsFragment()
    private var helpFragment: Fragment = HelpFragment()
    private var orderFragment: Fragment = OrdersFragment()
    private var favouriteFragment: Fragment = FavouriteFragment()
    private var accountFragment: Fragment = AccountFragment()
    private var cartFragment: Fragment = CartFragment()
    private var politicsFragment: Fragment = PoliticsAndPrivacityFragment()
    private var checkOrders: Fragment = CheckOrdersFragment()
    private var loginSuccess: Boolean = false
    private var signOut: Boolean = false
    private var email: String? = null
    private var googleLogin: Boolean = false
    private var userName: String? = ""
    private var userSurname: String? = ""
    private var imgUrl: String? =""
    private val db = FirebaseFirestore.getInstance()
    private var backPressedTime = 0L
    private var basketList = mutableListOf<ProductsDataClass>()
    private var visibleFloatingButton: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingHeader = HeaderLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("Add", getString(R.string.add))
        prefs.apply()

        binding.navigationView.menu[2].isVisible = BuildConfig.adminMode

        binding.floatingButton.visibility = View.INVISIBLE
        session()

        db.collection("Notificacion").addSnapshotListener{ value, e ->
            if (e != null) {
                Log.w("TAG", "Listen failed.", e)
                return@addSnapshotListener
            }
            binding.imageView7.visibility = View.VISIBLE
        }

        val emailHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.emailTextView) as TextView)
        val nameHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.user) as TextView)
        emailHeader.text = getString(R.string.signin_login)

        if (!loginSuccess || !googleLogin) {
            nameHeader.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val bundle = intent.extras
        if (bundle != null || loginSuccess) {
            if (bundle != null) {
                email = bundle.getString("email").toString()
                googleLogin = bundle.getBoolean("googleLogin")
            }
            emailHeader.text = email
            val photoHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.imageView) as ImageView)
            val name = db.collection("Usuario").document(email.toString()).get()
            name.addOnSuccessListener {
                if (it.exists()) {
                    userName = it.get("Nombre") as String?
                    userSurname = it.get("Apellidos") as String?
                    imgUrl = it.get("Img_url") as String?
                    val completeName = userName.toString() + " " + userSurname.toString()
                    nameHeader.text = completeName
                }
            }

            db.collection("Usuario").document(email!!).get().addOnSuccessListener {
                Picasso.get().load(it.get("Img_url") as String?).into(photoHeader)
            }

            db.collection("Usuario").document(email!!).collection("Carrito").addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                db.collection("Usuario").document(email!!).collection("Carrito").get().addOnSuccessListener { documents ->
                    val list = mutableListOf<ProductsDataClass>()


                    for (document in documents) {
                        val productObject = document.toObject(ProductsDataClass::class.java)
                        list.add(productObject)
                    }

                    setBasketObjects(list)
                    if (list.isEmpty()) {
                        binding.floatingButton.visibility = View.GONE
                        visibleFloatingButton = false
                    } else {
                        binding.floatingButton.visibility = View.VISIBLE
                        visibleFloatingButton = true

                    }
                }

                binding.floatingButton.setOnClickListener {
                    makeCurrentFragment(cartFragment)
                }
            }

            photoHeader.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                val inflater = layoutInflater
                val dialogLayout = inflater.inflate(R.layout.edit_text_layout, null)
                val editText = dialogLayout.findViewById<EditText>(R.id.editText)
                editText.setText(imgUrl)
                with(builder) {
                    setTitle(getString(R.string.change_img))
                    setPositiveButton(getString(R.string.okay)) { dialog, which ->
                        if (!editText.text.isNullOrEmpty()) {
                            db.collection("Usuario").document(email.toString()).set(
                                hashMapOf("Nombre" to userName,
                                "Apellidos" to userSurname,
                                "Email" to email,
                                "Img_url" to editText.text.toString())
                            )
                            finish()
                            startActivity(Intent(context, MainActivity::class.java))
                        } else {
                            Toast.makeText(context, getString(R.string.completeAllTheFields), Toast.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton(getString(R.string.different_account)){ dialog, which ->
                        Log.d("Main", "Negative button clicked")
                    }
                    setView(dialogLayout)
                    show()
                }
            }

            // Guardado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("email", email)
            prefs.putBoolean("googleLogin", googleLogin)
            prefs.apply()
        }

        makeCurrentFragment(homeFragment)

        binding.animatedBottomBar.onTabSelected = {
            fragmentSelected(it.id)
        }
        binding.animatedBottomBar.onTabReselected = {
            fragmentSelected(it.id)
        }
        binding.navigationView.setNavigationItemSelectedListener {
            fragmentSelected(it.itemId)
            openCloseNavigationDrawer()
        }

        val signOutButton =
            (binding.navigationView.getHeaderView(0).findViewById(R.id.signOut) as ImageButton)
        signOutButton.visibility = View.GONE
        if (loginSuccess) {
            signOutButton.visibility = View.VISIBLE
        }
        signOutButton.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            FirebaseAuth.getInstance().signOut()
            signOut = true
            onBackPressed()
            finish()
        }
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        email = prefs.getString("email", null)
        googleLogin = prefs.getBoolean("googleLogin", false)
        if (email != null) {
            loginSuccess = true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            val bundle = intent.extras
            if (bundle != null) {
                loginSuccess = bundle.getBoolean("loginSucces")
            }
            if (fragment == notificationFragment) {
                binding.imageView7.visibility = View.GONE
            }
            if ((fragment == orderFragment || fragment == favouriteFragment || fragment == accountFragment || fragment == cartFragment) && !loginSuccess) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if (visibleFloatingButton) {
                    binding.floatingButton.visibility = View.VISIBLE
                }
                replace(R.id.frame_container, fragment)
                commit()
            }
            if(fragment == cartFragment) {
                binding.floatingButton.visibility = View.GONE
            }
        }

    private fun openCloseNavigationDrawer(): Boolean {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
        return true
    }

    private fun fragmentSelected(id: Int): Boolean {
        when (id) {
            R.id.home -> makeCurrentFragment(homeFragment)
            R.id.notification -> makeCurrentFragment(notificationFragment)
            R.id.products -> makeCurrentFragment(productsFragment)
            R.id.menu -> openCloseNavigationDrawer()
            R.id.order -> makeCurrentFragment(orderFragment)
            R.id.favourites -> makeCurrentFragment(favouriteFragment)
            R.id.account -> makeCurrentFragment(accountFragment)
            R.id.basket ->makeCurrentFragment(cartFragment)
            R.id.politics ->makeCurrentFragment(politicsFragment)
            R.id.help -> makeCurrentFragment(helpFragment)
            R.id.seeOrders -> makeCurrentFragment(checkOrders)
        }
        return true
    }

    override fun onBackPressed() {
        if (loginSuccess && signOut) {
            loginSuccess = false
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, getString(R.string.press_again), Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.d("TAGOK", "onPaymentSucces: " + p0)
        Toast.makeText(applicationContext, getString(R.string.order_successful), Toast.LENGTH_SHORT).show()
        db.collection("Usuario").document(email!!).collection("Pedidos").document(p0!!).set(
            hashMapOf("id" to p0,
            "products" to basketList,
            "estado" to getString(R.string.waiting))
        )
        for (name in basketList) {
            db.collection("Usuario").document(email!!).collection("Carrito").document(name.Nombre).delete()
        }

        db.collection("Pedidos").document(p0).set(
            hashMapOf(
                "id" to p0,
                "email" to email,
                "products" to basketList,
                "estado" to getString(R.string.waiting)
            )
        )
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(applicationContext, getString(R.string.problem), Toast.LENGTH_SHORT).show()
    }

    private fun setBasketObjects(list: List<ProductsDataClass>) {
        basketList = list as MutableList<ProductsDataClass>
    }
}