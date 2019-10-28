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
package com.example.superWiserTVV2.useless

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.superWiserTVV2.Fragment
import com.example.superWiserTVV2.R

/**
 * This class demonstrates how to extend [android.support.v17.leanback.app.ErrorFragment].
 */
class ErrorFragmentEmailSent : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(R.string.app_name)

    }

    internal fun setErrorContent() {
        message = resources.getString(R.string.email_sent_message)
        messageSmall = resources.getString(R.string.email_sent_message_small)
        imageDrawable = ContextCompat.getDrawable(activity, R.drawable.email)
        setDefaultBackground(TRANSLUCENT)

        buttonText = resources.getString(R.string.dismiss_error)
        buttonClickListener = View.OnClickListener {
            fragmentManager.beginTransaction().remove(this@ErrorFragmentEmailSent).commit()
        }
    }

    companion object {
        private val TRANSLUCENT = true
    }
}