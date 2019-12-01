package com.example.superWiserTVV2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.superWiserTVV2.R

class SearchResultActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_fragment)
    }
    override fun onSearchRequested(): Boolean {
        startActivity(Intent(this, SearchResultActivity::class.java))
        return true
    }

}