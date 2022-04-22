package com.example.pintuapp

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.pintuapp.databinding.ActivityMainBinding
import com.example.pintuapp.databinding.HeaderLayoutBinding
import com.example.pintuapp.fragments.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingHeader: HeaderLayoutBinding
    private var homeFragment: Fragment = HomeFragment()
    private var notificationFragment: Fragment = NotificationFragment()
    private var helpFragment: Fragment = HelpFragment()
    private var orderFragment: Fragment = OrdersFragment()
    private var favouriteFragment: Fragment = FavouriteFragment()
    private var accountFragment: Fragment = AccountFragment()
    private var loginSuccess: Boolean = false
    private var signOut: Boolean = false
    private var email: String? = null
    private var googleLogin:Boolean = false
    private var photoUrl:String? = ""
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingHeader = HeaderLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session()

        val bundle = intent.extras

        if (bundle != null || loginSuccess) {
            if (bundle != null) {
                email = bundle.getString("email").toString()
                googleLogin = bundle.getBoolean("googleLogin")
                photoUrl = bundle.getString("photoUrl").toString()
            }
            val emailHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.emailTextView) as TextView)
            emailHeader.text = email
            val nameHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.user) as TextView)
            val photoHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.imageView) as ImageView)
            val name = db.collection("Usuario").document(email.toString()).get()
            name.addOnSuccessListener {
                if (it.exists()) {
                    val nombreCompleto = (it.get("Nombre") as String?) + " " + (it.get("Apellidos") as String?)
                    nameHeader.text = nombreCompleto
                }
            }
            if (googleLogin) {
                Picasso.get().load(photoUrl).into(photoHeader)
            }


            // Guardado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("email", email)
            prefs.putBoolean("googleLogin", googleLogin)
            prefs.putString("googlePhoto", photoUrl)
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

        val signOutButton = (binding.navigationView.getHeaderView(0).findViewById(R.id.signOut) as ImageButton)
        signOutButton.visibility = View.GONE
        if(loginSuccess) {
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
        photoUrl = prefs.getString("googlePhoto", null)
        if(email != null) {
            loginSuccess = true
        }
    }

    private fun makeCurrentFragment(fragment : Fragment) = supportFragmentManager.beginTransaction().apply {
        val bundle = intent.extras
        if (bundle != null) {
            loginSuccess = bundle.getBoolean("loginSucces")
        }
        if((fragment == orderFragment || fragment == favouriteFragment || fragment == accountFragment) && !loginSuccess) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            replace(R.id.frame_container, fragment)
            commit()
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
        when(id) {
            R.id.home -> makeCurrentFragment(homeFragment)
            R.id.notification -> makeCurrentFragment(notificationFragment)
            R.id.help -> makeCurrentFragment(helpFragment)
            R.id.menu -> openCloseNavigationDrawer()
            R.id.order -> makeCurrentFragment(orderFragment)
            R.id.favourites -> makeCurrentFragment(favouriteFragment)
            R.id.account -> makeCurrentFragment(accountFragment)
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (loginSuccess && signOut) {
            loginSuccess = false
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}