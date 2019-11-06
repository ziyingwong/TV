package com.example.superWiserTVV2.halfway

import android.os.Bundle
import android.support.v17.leanback.app.VerticalGridFragment
import android.support.v17.leanback.widget.ArrayObjectAdapter
import android.support.v17.leanback.widget.VerticalGridPresenter
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewMoreFragment : VerticalGridFragment() {
    var NUM_COLUMN = 4;
    var mAdapter = ArrayObjectAdapter()
    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    var scenes = ArrayList<Scene>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = activity.intent.getStringExtra("groupname")
        var admin = activity.intent.getStringExtra("admin")
        var groupId = activity.intent.getStringExtra("groupid")
        db.collection("scene").whereEqualTo("admin", admin)
            .whereEqualTo("group",groupId).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("myTag","Listen Failed ${e}")
                    return@addSnapshotListener
                }
                if(snapshot!=null){
                    scenes.clear()
                    for (doc in snapshot.documents) {
                        var scene = doc.toObject(Scene::class.java)
                        scenes.add(scene as Scene)
                        Log.e("myTag", scene.thumbnail)
                    }
                    setupFragment()
                }else{
                    Log.e("myTag", "snapshot null")
                }
            }
        setupFragment()
    }

    fun setupFragment(){
        var gridPresenter = VerticalGridPresenter()
        gridPresenter.numberOfColumns = NUM_COLUMN
        setGridPresenter(gridPresenter)
        var cardPresenter = CardPresenter()
        mAdapter = ArrayObjectAdapter(cardPresenter)
        for(scene in scenes){
            mAdapter.add(scene)
        }
        adapter = mAdapter
    }



}