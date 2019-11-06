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

import java.util.Timer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.widget.*
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.example.superWiserTVV2.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseFragment() {

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    private var mBackgroundTimer: Timer? = null
    var groups = ArrayList<Group>()
    var playgroups = ArrayList<Group>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)
        db.collection("Group").whereEqualTo("admin", auth.currentUser!!.uid)
            .whereArrayContains("user", auth.currentUser!!.uid).addSnapshotListener { snapshot, e ->
                if (e != null) {
                Log.e("myTag","Listen Failed ${e}")
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    groups.clear()
                    for (doc in snapshot.documents) {
                        var group = doc.toObject(Group::class.java)
                        groups.add(group as Group)
                        Log.e("myTag", group.name)
                    }
                    loadRows()
                }else{
                    Log.e("myTag", "snapshot null")
                }
            }
        db.collection("PlayGroup").whereEqualTo("admin", auth.currentUser!!.uid)
            .whereArrayContains("user", auth.currentUser!!.uid).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("myTag","Listen Failed ${e}")
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    playgroups.clear()
                    for (doc in snapshot.documents) {
                        var playgroup = doc.toObject(Group::class.java)
                        playgroups.add(playgroup as Group)
                        Log.e("myTag", playgroup.name)
                    }
                    loadRows()
                }else{
                    Log.e("myTag", "snapshot null")
                }
            }
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
        headersState = BrowseFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = ContextCompat.getColor(activity, R.color.fastlane_background)
        searchAffordanceColor = ContextCompat.getColor(activity, R.color.search_opaque)
    }


    private fun loadRows() {
        val cardPresenter = CardPresenter()
        val groupRowAdapter = ArrayObjectAdapter(cardPresenter)
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        var viewMorecard = Card()
        viewMorecard.imageUrl = "https://cdn.icon-icons.com/icons2/1509/PNG/512/viewmorehorizontal_104501.png"
        viewMorecard.title = "View More"
        if (groups.size > NUM_COLS) {
            for (j in 0 until NUM_COLS) {
                groupRowAdapter.add(groups[j])
            }
            groupRowAdapter.add(viewMorecard)
        } else {
            for (j in 0 until groups.size) {
                groupRowAdapter.add(groups[j])
            }
        }
        val groupheader = HeaderItem(0, "Group")
        rowsAdapter.add(ListRow(groupheader, groupRowAdapter))

        val playgroupRowAdapter = ArrayObjectAdapter(cardPresenter)

        if (playgroups.size > NUM_COLS) {
            for (j in 0 until NUM_COLS) {
                playgroupRowAdapter.add(playgroups[j])
            }
            playgroupRowAdapter.add(viewMorecard)

        } else {
            for (j in 0 until playgroups.size) {
                playgroupRowAdapter.add(playgroups[j])
            }
        }
        val playgroupheader = HeaderItem(0, "Play Group")
        rowsAdapter.add(ListRow(playgroupheader, playgroupRowAdapter))

        val gridHeader = HeaderItem(1, "Settings")
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

            if (item is Group) {
                Log.d(TAG, "Item: " + item.toString())
                val intent = Intent(activity,ViewMoreActivity::class.java)
                intent.putExtra("groupid",item.id)
                intent.putExtra("groupname",item.name)
                intent.putExtra("admin",item.admin)
                activity.startActivity(intent)
//                val intent = Intent(activity, DetailsActivity::class.java)
//                intent.putExtra(DetailsActivity.MOVIE, item)
//                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity,
//                    (itemViewHolder.view as ImageCardView).mainImageView,
//                    DetailsActivity.SHARED_ELEMENT_NAME
//                )
//                    .toBundle()
//                activity.startActivity(intent, bundle)
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
        private val NUM_COLS = 7
    }


}
