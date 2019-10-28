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

import java.util.Collections
import java.util.Timer
import java.util.TimerTask

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v17.leanback.app.BackgroundManager
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.HeaderItem
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.ListRow
import android.support.v17.leanback.widget.ListRowPresenter
import android.support.v17.leanback.widget.OnItemViewClickedListener
import android.support.v17.leanback.widget.OnItemViewSelectedListener
import android.support.v17.leanback.widget.Presenter
import android.support.v17.leanback.widget.Row
import android.support.v17.leanback.widget.RowPresenter
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.example.superWiserTVV2.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseFragment() {

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    private var mBackgroundTimer: Timer? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)
        db.collection("Group").whereEqualTo("admin", auth.currentUser!!.uid)
            .whereArrayContains("user", auth.currentUser!!.uid).get().addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    Log.e("myTag", doc.get("name").toString())
                }
            }
        var query = db.collection("Group").whereEqualTo("admin", auth.currentUser!!.uid)
            .whereArrayContains("user", auth.currentUser!!.uid)

//        db.collection("Group").whereEqualTo("admin", DataContainer.admin)
//            .whereArrayContains("user", auth.currentUser!!.uid).get().addOnSuccessListener { snapshot ->
//            for (doc in snapshot.documents) {
//                Log.e("myTag",doc.get("name").toString())
//            }
//        }
        setupUIElements()

        loadRows()

        setupEventListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: " + mBackgroundTimer?.toString())
        mBackgroundTimer?.cancel()
    }


    private fun setupUIElements() {
        title = getString(R.string.browse_title)

        // over title
        headersState = BrowseFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(activity, R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(activity, R.color.search_opaque)
    }



    private fun loadRows() {

        val list = MovieList.list

        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        for (i in 0 until NUM_ROWS) {
            if (i != 0) {
                Collections.shuffle(list)
            }
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            for (j in 0 until NUM_COLS) {
                listRowAdapter.add(list[j % 5])
            }
            val header = HeaderItem(i.toLong(), MovieList.MOVIE_CATEGORY[i])
            rowsAdapter.add(ListRow(header, listRowAdapter))
        }
        val gridHeader1 = HeaderItem(NUM_ROWS.toLong(), "Group")
        val gridRowAdapter1 = ArrayObjectAdapter(cardPresenter)
        for (j in 0 until NUM_COLS-1) {
            gridRowAdapter1.add(list[j%5])
        }
        val viewMore = Movie()
        viewMore.title = "View more"
        viewMore.cardImageUrl ="https://cdn.icon-icons.com/icons2/1509/PNG/512/viewmorehorizontal_104501.png"
        gridRowAdapter1.add(viewMore)
        rowsAdapter.add(ListRow(gridHeader1, gridRowAdapter1))

        val gridHeader = HeaderItem(NUM_ROWS.toLong(), "Settings")
        val mGridPresenter = GridItemPresenter()
        val gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
        gridRowAdapter.add(resources.getString(R.string.logout))
        rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(activity, "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

            if (item is Movie) {
                Log.d(TAG, "Item: " + item.toString())
                val intent = Intent(activity, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item)

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    DetailsActivity.SHARED_ELEMENT_NAME
                )
                    .toBundle()
                activity.startActivity(intent, bundle)
            } else if (item is Group) {
                Log.e("myTag", "clicked ${item.name}")
            } else if (item is String) {
                if (item.contains(resources.getString(R.string.logout))) {
                    auth.signOut()
                    val intent = Intent(activity, Login::class.java)
                    activity.finishAffinity()
                    startActivity(intent)
                }
            }
        }
    }


    private inner class GridItemPresenter : Presenter() {
        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
            val view = TextView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(
                GRID_ITEM_WIDTH,
                GRID_ITEM_HEIGHT
            )
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.setBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.default_background
                )
            )
            view.setTextColor(Color.WHITE)
            view.gravity = Gravity.CENTER
            return Presenter.ViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
            (viewHolder.view as TextView).text = item as String
        }

        override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {}
    }

    companion object {
        private val TAG = "MainFragment"
        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200
        private val NUM_ROWS = 2
        private val NUM_COLS = 10
    }
}
