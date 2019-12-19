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

import android.Manifest
import java.util.Timer

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v17.leanback.app.BrowseFragment
import android.support.v17.leanback.app.SearchFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.content.ActivityNotFoundException
import android.support.v17.leanback.widget.SpeechRecognitionCallback
import android.Manifest.permission
import android.Manifest.permission.RECORD_AUDIO
import android.app.AlertDialog
import android.app.Fragment
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot


/**
 * Loads a grid of cards with movies to browse.
 */
class SearchResultFragment : SearchFragment(), SearchFragment.SearchResultProvider {
    var cardPresenter = CardPresenter()

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    private var mBackgroundTimer: Timer? = null
    var groups = ArrayList<Group>()
    var playgroups = ArrayList<PlayGroup>()
    var scenes = ArrayList<Scene>()
    var mRowsAdapter: ArrayObjectAdapter = ArrayObjectAdapter(ListRowPresenter())
    val REQUEST_SPEECH = 0x00000010


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (ContextCompat.checkSelfPermission(
//                activity,
//                Manifest.permission.RECORD_AUDIO
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
//        } else {
//        setSpeechRecognitionCallback(object : SpeechRecognitionCallback {
//            override fun recognizeSpeech() {
//                try {
//                    startActivityForResult(recognizerIntent, REQUEST_SPEECH)
//                } catch (e: ActivityNotFoundException) {
//                    Log.e("mytag", "$e")
//                }
//            }
//        })
        setSearchResultProvider(this)
        setOnItemViewClickedListener(ItemViewClickedListener())
//        }

    }


    override fun onQueryTextSubmit(s: String?): Boolean {
        groups.clear()
        playgroups.clear()
        scenes.clear()
        mRowsAdapter.clear()

        if (!s.isNullOrBlank()) {
            db.collection("Group").whereArrayContains("user", auth.currentUser!!.uid).get()
                .addOnSuccessListener { group ->
                    for (g in group.documents) {

                        db.collection("scene").whereEqualTo("group", g.id).orderBy("lowercasename")
                            .startAt(s)
                            .endAt(s.replace("\\s".toRegex(), "").toLowerCase() + "\uf8ff").get()
                            .addOnSuccessListener { scenesSnapshot ->
                                if (!scenesSnapshot.documents.isNullOrEmpty()) {
                                    for (scene in scenesSnapshot) {
                                        scenes.add(scene.toObject(Scene::class.java))
                                    }
                                    resultsAdapter

                                    Log.e("mytag", "scene ${scenes.size}")
                                } else {
                                    Log.e("mytag", "no match scene")
                                }

                            }
                            .addOnFailureListener { e ->
                                Log.e("mytag", "$e")
                            }
                    }

                }
            db.collection("Group").whereArrayContains("user", auth.currentUser!!.uid)
                .orderBy("lowercasename")
                .startAt(s)
                .endAt(s?.replace("\\s".toRegex(), "").toLowerCase() + "\uf8ff").get()
                .addOnSuccessListener { groupSnapshot ->
                    if (!groupSnapshot.documents.isNullOrEmpty()) {
                        for (doc in groupSnapshot) {
                            var group = doc.toObject(Group::class.java)
                            groups.add(group)
                        }
                        resultsAdapter
                        Log.e("mytag", "group ${groups.size}")
                    } else {
                        Log.e("mytag", "no match group")

                    }
                }
                .addOnFailureListener { e ->
                    Log.e("mytag", "$e")
                }


            db.collection("PlayGroup").whereArrayContains("user", auth.currentUser!!.uid)
                .orderBy("lowercasename")
                .startAt(s)
                .endAt(s.replace("\\s".toRegex(), "").toLowerCase() + "\uf8ff").get()
                .addOnSuccessListener { playGroupSnapshot ->
                    if (!playGroupSnapshot.documents.isNullOrEmpty()) {
                        for (doc in playGroupSnapshot) {

                            //load playgroup image from scene
                            var playgroup = doc.toObject(PlayGroup::class.java)
                            playgroups.add(playgroup)
                        }
                        resultsAdapter
                        Log.e("mytag", "playgroup ${playgroups.size}")

                    } else {
                        Log.e("mytag", "no match playgroup")

                    }
                }
                .addOnFailureListener { e ->
                    Log.e("mytag", "$e")
                }
            return true
        } else {
            return false
        }

        return true
    }

    override fun getResultsAdapter(): ObjectAdapter {
        mRowsAdapter.clear()
        var groupRowAdapter = ArrayObjectAdapter(cardPresenter)
        var sceneRowAdapter = ArrayObjectAdapter(cardPresenter)
        var playgroupRowAdapter = ArrayObjectAdapter(cardPresenter)
        Log.e("mytag", groups.size.toString())
        if (groups.isNotEmpty()) {
            for (j in 0 until groups.size) {
                groupRowAdapter.add(groups[j])
            }
            val groupheader = HeaderItem(0, "Group")
            mRowsAdapter.add(ListRow(groupheader, groupRowAdapter))
        }
        if (playgroups.isNotEmpty()) {
            for (j in 0 until playgroups.size) {
                playgroupRowAdapter.add(playgroups[j])
            }
            val playgroupheader = HeaderItem(0, "Play Group")
            mRowsAdapter.add(ListRow(playgroupheader, playgroupRowAdapter))
        }
        if (scenes.isNotEmpty()) {
            for (j in 0 until scenes.size) {
                sceneRowAdapter.add(scenes[j])
            }
            val sceneheader = HeaderItem(0, "Scene")
            mRowsAdapter.add(ListRow(sceneheader, sceneRowAdapter))
        }
        return mRowsAdapter
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            if (item is Group) {
                val intent = Intent(activity, ViewVerticalGridActivity::class.java)
                intent.putExtra("groupid", item.id)
                intent.putExtra("viewingPageName", item.name)
                intent.putExtra("type", "group")
                activity.startActivity(intent)

            } else if (item is PlayGroup) {
                val intent = Intent(activity, PlaySceneActivity::class.java)
                db.collection("scene").whereArrayContains("playgroup", item.id).get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.documents.size < 1) {
                            AlertDialog.Builder(activity)
                                .setTitle("Empty Playgroup")
                                .setMessage("This is an empty playgroup")
                                .setNeutralButton("Dismiss", null)
                                .show()
                        } else {
                            for (doc in snapshot.documents) {
                                var scene = doc.toObject(Scene::class.java)
                                DataContainer.playscene.add(scene!!)
                            }
                            activity.startActivity(intent)
                        }

                    }.addOnFailureListener { e ->
                    Log.e("mytag", "$e")
                    AlertDialog.Builder(activity)
                        .setTitle("Error")
                        .setMessage("Error occurred when loading playgroup. \n$e")
                        .setNeutralButton("Dismiss", null)
                        .show()
                }
            } else if (item is Scene) {
                val intent = Intent(activity, GroupScene::class.java)
                intent.putExtra("id", item.id)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                activity.startActivity(intent)
            }
        }
    }

}
