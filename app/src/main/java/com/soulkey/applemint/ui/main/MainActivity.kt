package com.soulkey.applemint.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.soulkey.applemint.R
import com.soulkey.applemint.ui.login.LoginActivity
import com.soulkey.applemint.ui.main.bookmark.BookmarkFragment
import com.soulkey.applemint.ui.main.article.NewArticleFragment
import com.soulkey.applemint.ui.main.article.ReadLaterFragment
import com.soulkey.applemint.ui.main.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val mainViewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mainViewModel.isFilterOpen.value = false
        navigation_main.setNavigationItemSelectedListener(this)

        iv_btn_menu.setOnClickListener {
            drawer_main.openDrawer(GravityCompat.START)
        }

        iv_article_filter.setOnClickListener {
            mainViewModel.isFilterOpen.value = !mainViewModel.isFilterOpen.value!!
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main_body,
                DashboardFragment()
            )
        }.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_home-> {
                tv_main_title.text = getString(R.string.app_name)
                iv_article_filter.visibility = View.INVISIBLE
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container_main_body,
                        DashboardFragment()
                    )
                }.commit()
            }
            R.id.item_new_article->{
                tv_main_title.text = getString(R.string.articles)
                iv_article_filter.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container_main_body, NewArticleFragment())
                }.commit()
            }
            R.id.item_read_later->{
                tv_main_title.text = getString(R.string.read_later)
                iv_article_filter.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container_main_body, ReadLaterFragment())
                }.commit()
            }
            R.id.item_bookmark->{
                tv_main_title.text = getString(R.string.item_bookmark)
                iv_article_filter.visibility = View.VISIBLE
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container_main_body,
                        BookmarkFragment()
                    )
                }.commit()
            }
            R.id.item_logout ->{
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
