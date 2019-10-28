package com.soulkey.applemint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.soulkey.applemint.ui.main.MainViewModel
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val mainViewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mainViewModel.initialize()
        navigation_main.setNavigationItemSelectedListener(this)

        iv_btn_menu.setOnClickListener {
            drawer_main.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_logout->{
                Timber.v("diver:/ call on navigation sign out")
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }
        drawer_main.closeDrawer(GravityCompat.START)
        return true
    }
}
