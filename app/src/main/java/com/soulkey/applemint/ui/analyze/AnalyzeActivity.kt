package com.soulkey.applemint.ui.analyze

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.soulkey.applemint.R
import kotlinx.android.synthetic.main.analyze_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyzeActivity : AppCompatActivity(){
    private val viewModel: AnalyzeViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.analyze_activity)
        viewModel.callAnalyze(intent.getStringExtra("id"))
        viewModel.targetTitle.observe(this, Observer {text->
            tv_content_analyzed_title.text = text
        })
    }
}