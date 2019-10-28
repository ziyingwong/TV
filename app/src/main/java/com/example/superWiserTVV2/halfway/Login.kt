/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.superWiserTVV2.halfway

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.superWiserTVV2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Login : Activity() {

    //link to server

    var auth = FirebaseAuth.getInstance()
    var cookiemanager = CookieManager.getInstance()
    lateinit var ipAdd: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        var emailEditText = findViewById<EditText>(R.id.emailEditText)
        var passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        var loginButton = findViewById<Button>(R.id.loginButton)
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        loginButton.setOnClickListener {
            Log.e("myTag", "Loading : Login")
            progressBar.bringToFront()
            progressBar.visibility = View.VISIBLE
            emailEditText.isEnabled = false
            passwordEditText.isEnabled = false
            loginButton.isClickable = false
            var email = emailEditText.text.toString()
            var password = passwordEditText.text.toString()
            if (email.isNullOrBlank() || password.isNullOrBlank()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.INVISIBLE
                emailEditText.isEnabled = true
                passwordEditText.isEnabled = true
                loginButton.isClickable = true
            } else {
                email = email.replace("\\s".toRegex(), "")
                password = password.replace("\\s".toRegex(), "")

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.e("myTag", "Success : Login")
                            var UID = auth.currentUser!!.uid
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)

                            //API call to login to ThingsFactory
//                                    val url = "http://${ipAdd}:300/login/"
//                                    val jsonObjectRequest = JsonObjectRequest(
//                                        Request.Method.GET,
//                                        url,
//                                        null,
//                                        Response.Listener { response ->
//                                            var token = response.getString("access_token")
//                                            Log.e("myTag", "token ${token}")
//                                            val sharedPref =
//                                                this.getSharedPreferences("pref", 0)
//                                            val editor = sharedPref.edit()
//                                            editor.putString("token", token)
//                                            editor.apply()
//                                            cookiemanager.setCookie(
//                                                "http://${ipAdd}:3000",
//                                                "access_token=${token}"
//                                            )
//

//                                                val intent = Intent(this, MainActivity::class.java)
//                                                startActivity(intent)

//                                        },
//                                        Response.ErrorListener { error ->
//                                            Log.e("myTag", "Error : ${error}")
//                                            var builder = AlertDialog.Builder(this)
//                                            builder.setTitle("Fail to login")
//                                            builder.setMessage("Server is off. Please try again later")
//                                            builder.setNeutralButton("Ok") { dialog, which ->
//                                                auth.signOut()
//                                                finishAffinity()
//                                                startActivity(Intent(this, General_Login::class.java))
//                                            }
//                                            builder.show()
//                                        }
//                                    )
//                                    Singleton_Volley.getInstance(this@General_Login)
//                                        .addToRequestQueue(jsonObjectRequest)

                        } else {
                            Log.e("myTag", "Failure : ${task.exception}")
                            var intent = Intent(this, InvalidPassword::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                            intent.putExtra("email", email)
                            startActivity(intent)
                        }
                    }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        var emailEditText = findViewById<EditText>(R.id.emailEditText)
        var passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        var loginButton = findViewById<Button>(R.id.loginButton)
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE
        emailEditText.isEnabled = true
        passwordEditText.isEnabled = true
        loginButton.isClickable = true
    }


    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Log.e("myTag", "welcome back")
            var emailEditText = findViewById<EditText>(R.id.emailEditText)
            var passwordEditText = findViewById<EditText>(R.id.passwordEditText)
            var loginButton = findViewById<Button>(R.id.loginButton)
            var progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.bringToFront()
            progressBar.visibility = View.VISIBLE
            emailEditText.isEnabled = false
            passwordEditText.isEnabled = false
            loginButton.isClickable = false
            val sharedPref = this.getSharedPreferences("pref", 0)
            var token = sharedPref.getString("token", "")
            if (token.isNullOrBlank()) {
                Log.e("myTag", "sorry no token")
                auth.signOut()
                finishAffinity()
                startActivity(Intent(this, Login::class.java))
            } else {
                cookiemanager.setCookie(
                    "http://${ipAdd}:3000",
                    "access_token=${token}"
                )
                Log.e("myTag", "token : ${token}")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}


