package com.example.superWiserTVV2.halfway

import android.app.Activity
import android.os.Bundle
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
}