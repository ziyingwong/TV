package com.example.superWiserTVV2

import android.content.Intent
import android.os.Bundle
import android.support.v17.leanback.app.VerticalGridFragment
import android.support.v17.leanback.widget.*
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewVerticalGridFragment : VerticalGridFragment() {
    var NUM_COLUMN = 4;
    var mAdapter = ArrayObjectAdapter()
    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    var scenes = ArrayList<Scene>()
    var playgroups = ArrayList<PlayGroup>()
    var groups = ArrayList<Group>()
    lateinit var viewingPageName: String
    lateinit var type: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewingPageName = activity.intent.getStringExtra("viewingPageName")
        type = activity.intent.getStringExtra("type")
        if (viewingPageName.equals("viewmore")) {
            if (type.equals("group")) {
                title = "Group"
                db.collection("Group").whereArrayContains("user", auth.currentUser!!.uid)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.e("myTag", "Listen Failed ${e}")
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            groups.clear()
                            for (doc in snapshot.documents) {
                                var group = doc.toObject(Group::class.java)
                                groups.add(group as Group)
                                Log.e("myTag", group.name)
                            }
                            setupFragment()
                        } else {
                            Log.e("myTag", "snapshot null")
                        }
                    }
            } else if (type.equals("playgroup")) {
                title = "Play Group"
                db.collection("PlayGroup").whereArrayContains("user", auth.currentUser!!.uid)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.e("myTag", "Listen Failed ${e}")
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            playgroups.clear()
                            for (doc in snapshot.documents) {
                                var playgroup = doc.toObject(PlayGroup::class.java)
                                playgroups.add(playgroup as PlayGroup)
                                Log.e("myTag", playgroup.name)
                            }
                            setupFragment()
                        } else {
                            Log.e("myTag", "snapshot null")
                        }
                    }
            } else {
                Log.e("myTag", "Invalid type of group : not group nor playgroup")
            }

        } else {
            title = viewingPageName
            var groupId = activity.intent.getStringExtra("groupid")
            if (type.equals("group")) {
                db.collection("scene").whereEqualTo("group", groupId)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.e("myTag", "Listen Failed ${e}")
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            scenes.clear()
                            for (doc in snapshot.documents) {
                                var scene = doc.toObject(Scene::class.java)
                                scenes.add(scene as Scene)
                            }
                            setupFragment()
                        } else {
                            Log.e("myTag", "snapshot null")
                        }
                    }
            } else if (type.equals("playgroup")) {
                db.collection("scene").whereArrayContains("playgroup", groupId)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.e("myTag", "Listen Failed ${e}")
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            scenes.clear()
                            for (doc in snapshot.documents) {
                                var scene = doc.toObject(Scene::class.java)
                                scenes.add(scene as Scene)
                            }
                            setupFragment()
                        } else {
                            Log.e("myTag", "snapshot null")
                        }
                    }
            } else {
                Log.e("myTag", "Invalid type of group : not group nor playgroup")
            }

        }

        setupFragment()
        setEventListener()
    }

    fun setupFragment() {
        var gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = NUM_COLUMN
        setGridPresenter(gridPresenter)
        var cardPresenter = CardPresenter()
        mAdapter = ArrayObjectAdapter(cardPresenter)
        if (viewingPageName.equals("viewmore")) {
            if (type.equals("group")) {
                for (group in groups) {
                    mAdapter.add(group)
                }
            } else {
                for (playgroup in playgroups) {
                    mAdapter.add(playgroup)
                }
            }
        } else {
            for (scene in scenes) {
                mAdapter.add(scene)
            }
        }
        adapter = mAdapter
    }

    fun setEventListener() {
        onItemViewClickedListener = object : OnItemViewClickedListener {
            override fun onItemClicked(
                p0: Presenter.ViewHolder?,
                p1: Any?,
                p2: RowPresenter.ViewHolder?,
                p3: Row?
            ) {
                if (p1 != null) {
                    if (p1 is Scene) {
                        if (type.equals("group")) {
                            val intent = Intent(activity, GroupScene::class.java)
                            intent.putExtra("id", p1.id)
                            activity.startActivity(intent)
                        } else if (type.equals("playgroup")) {
                            Log.e("myTag", "playgroup scene${p1.name}")
                        }
                    } else if (p1 is Group) {
                        val intent = Intent(activity, ViewVerticalGridActivity::class.java)
                        intent.putExtra("groupid", p1.id)
                        intent.putExtra("viewingPageName", p1.name)
                        intent.putExtra("type", "group")
                        activity.startActivity(intent)
                    } else if (p1 is PlayGroup) {
                        val intent = Intent(activity, ViewVerticalGridActivity::class.java)
                        intent.putExtra("groupid", p1.id)
                        intent.putExtra("viewingPageName", p1.name)
                        intent.putExtra("type", "playgroup")
                        activity.startActivity(intent)
                    }
                }
            }

        }
    }




}