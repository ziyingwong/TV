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

package com.example.superWiserTVV2

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import android.view.ViewGroup

import com.bumptech.glide.Glide
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class CardPresenter : Presenter() {

    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor = ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.empty)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val cardView = viewHolder.view as ImageCardView
        cardView.setMainImageDimensions(
            CARD_WIDTH,
            CARD_HEIGHT
        )
        if (item is Group) {

            val group = item
            cardView.titleText = group.name

            if (item.imageUrl.isNullOrBlank()) {
                Glide.with(viewHolder.view.context)
                    .load("https://www.generationsforpeace.org/wp-content/uploads/2018/03/empty.jpg")
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.mainImageView)
            } else {
                var encodedString = item.imageUrl.trim()
                encodedString = encodedString.replace("data:image/png;base64,", "")
                Log.e("mytag",encodedString)
                var decodedString = Base64.decode(encodedString, Base64.DEFAULT) as ByteArray
                var bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                cardView.mainImageView.setImageBitmap(bitmap)
            }

        } else if (item is PlayGroup) {
            cardView.titleText = item.name
            if (item.imageUrl.isNullOrBlank()) {
                Glide.with(viewHolder.view.context)
                    .load("https://www.generationsforpeace.org/wp-content/uploads/2018/03/empty.jpg")
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.mainImageView)
            } else {
                var encodedString = item.imageUrl.trim()
                encodedString = encodedString.replace("data:image/png;base64,", "")
                var decodedString = Base64.decode(encodedString, Base64.DEFAULT) as ByteArray
                var bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                cardView.mainImageView.setImageBitmap(bitmap)
            }

        } else if (item is Card) {
            cardView.titleText = item.title
            Glide.with(viewHolder.view.context)
                .load(item.imageUrl)
                .centerCrop()
                .error(mDefaultCardImage)
                .into(cardView.mainImageView)
        } else if (item is Scene) {
            cardView.titleText = item.name
            var encodedString = item.thumbnail.trim()
            encodedString = encodedString.replace("data:image/png;base64,", "")
            var decodedString = Base64.decode(encodedString, Base64.DEFAULT) as ByteArray
            var bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            cardView.mainImageView.setImageBitmap(bitmap)

        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private val CARD_WIDTH = 313
        private val CARD_HEIGHT = 176
    }


}
