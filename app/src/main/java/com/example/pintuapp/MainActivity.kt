package com.example.pintuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.pintuapp.databinding.ActivityMainBinding
import com.example.pintuapp.fragments.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private var homeFragment: Fragment = HomeFragment()
    private var notificationFragment: Fragment = NotificationFragment()
    private var helpFragment: Fragment = HelpFragment()
    private var orderFragment: Fragment = OrdersFragment()
    private var favouriteFragment: Fragment = FavouriteFragment()
    private var accountFragment: Fragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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
        replace(R.id.frame_container, fragment)
        commit()
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

}
