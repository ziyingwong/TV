package com.example.superWiserTVV2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.example.superWiserTVV2.R

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onSearchRequested(): Boolean {
        startActivity(Intent(this, SearchResultActivity::class.java))
        return true
    }
}