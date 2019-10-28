package com.example.superWiserTVV2

import android.app.Activity
import android.os.Bundle
import android.widget.Button

class EmailSent : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emailsent)
        var button = findViewById<Button>(R.id.okButton)
        button.setOnClickListener {
            finish()
        }

    }
}