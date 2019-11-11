package com.soulkey.applemint.ui.main.article

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.viewer_activity.*

class ViewerActivity : AppCompatActivity() {
    lateinit var articleUrl : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewer_activity)
        articleUrl = intent.getStringExtra("url")
        webview.webViewClient = WebViewClient()
        webview.settings.javaScriptEnabled = true
        webview.settings.useWideViewPort = true
        webview.settings.setSupportMultipleWindows(false)
        webview.loadUrl(articleUrl)
    }
}