package com.soulkey.applemint.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.DialogTitle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.soulkey.applemint.R
import com.soulkey.applemint.ui.login.LoginActivity
import com.soulkey.applemint.ui.main.article.ArticleViewModel
import com.soulkey.applemint.ui.main.bookmark.BookmarkFragment
import com.soulkey.applemint.ui.main.article.NewArticleFragment
import com.soulkey.applemint.ui.main.article.ReadLaterFragment
import com.soulkey.applemint.ui.main.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.main_activity.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val mainViewModel: MainViewModel by viewModel()
    private val articleViewModel:  ArticleViewModel by viewModel()
    lateinit var currentFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mainViewModel.isFilterOpen.value = false
        navigation_main.setNavigationItemSelectedListener(this)

        iv_btn_menu.setOnClickListener {
            drawer_main.openDrawer(GravityCompat.START)
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).also { imm->
                currentFocus?.let {imm.hideSoftInputFromWindow(it.windowToken, 0)}
            }
        }

        iv_article_refresh.setOnClickListener {
            articleViewModel.fetchArticles()
        }
        iv_article_filter.setOnClickListener {
            mainViewModel.isFilterOpen.value = !mainViewModel.isFilterOpen.value!!
        }
        mainViewModel.isFilterApply.observe(this, Observer {
            iv_filter_notification_dot.visibility = if (it) View.VISIBLE else View.INVISIBLE
        })
        currentFragment = DashboardFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main_body, currentFragment)
        }.commit()

        drawer_main.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerStateChanged(newState: Int) {}
            override fun onDrawerOpened(drawerView: View) {}
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerClosed(drawerView: View) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container_main_body, currentFragment)
                }.commit()
            }
        })
    }

    override fun onBackPressed() {
        MaterialDialog(this).show {
            title(text = "Want to close Applemint?")
            positiveButton { super.onBackPressed() }
            negativeButton {  }
            cornerRadius(16f)
        }
    }

    private fun replaceFragment(fragment: Fragment, titleText: String = "Applemint", setFilterVisible:Boolean = false, setRefreshVisible:Boolean = false) {
        tv_main_title.text = titleText
        iv_filter_notification_dot.visibility = View.INVISIBLE
        iv_article_filter.visibility = if (setFilterVisible) View.VISIBLE else View.INVISIBLE
        iv_article_refresh.visibility = if (setRefreshVisible) View.VISIBLE else View.INVISIBLE
        currentFragment = fragment
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_home->
                replaceFragment(DashboardFragment())
            R.id.item_new_article->
                replaceFragment(NewArticleFragment(), titleText = getString(R.string.articles), setFilterVisible = true, setRefreshVisible = true)
            R.id.item_read_later->
                replaceFragment(ReadLaterFragment(), titleText = getString(R.string.read_later), setFilterVisible = true, setRefreshVisible = true)
            R.id.item_bookmark->
                replaceFragment(BookmarkFragment(), titleText = getString(R.string.item_bookmark), setFilterVisible = true)
            R.id.item_logout ->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
        }
        drawer_main.closeDrawer(GravityCompat.START)
        return true
    }
}
