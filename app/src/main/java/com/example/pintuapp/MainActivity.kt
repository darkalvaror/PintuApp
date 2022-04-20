package com.example.pintuapp

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.pintuapp.databinding.ActivityMainBinding
import com.example.pintuapp.databinding.HeaderLayoutBinding
import com.example.pintuapp.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingHeader: HeaderLayoutBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var homeFragment: Fragment = HomeFragment()
    private var notificationFragment: Fragment = NotificationFragment()
    private var helpFragment: Fragment = HelpFragment()
    private var orderFragment: Fragment = OrdersFragment()
    private var favouriteFragment: Fragment = FavouriteFragment()
    private var accountFragment: Fragment = AccountFragment()
    private var loginSuccess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingHeader = HeaderLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingHeader.emailTextView.text = "Texto de ejemplo"
        bindingHeader.user.text = "Nombre del usuario"
        bindingHeader.imageView.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.google_icon))



        val bundle = intent.extras
        if (bundle != null) {
            val email = bundle.getString("email")
            val googleLogin = bundle.getBoolean("googleLogin")
            val emailHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.emailTextView) as TextView)
            emailHeader.text = email
            val nameHeader = (binding.navigationView.getHeaderView(0).findViewById(R.id.user) as TextView)
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
    }

    private fun makeCurrentFragment(fragment : Fragment) = supportFragmentManager.beginTransaction().apply {
        val bundle = intent.extras
        if (bundle != null) {
            loginSuccess = bundle.getBoolean("loginSucces")
        }
        if((fragment == orderFragment || fragment == favouriteFragment || fragment == accountFragment) && !loginSuccess) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
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

        val hView:View = binding.navigationView.getHeaderView(0)

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
}