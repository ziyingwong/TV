package com.example.superWiserTVV2

import android.os.Parcelable
import java.io.Serializable

class Group {
    lateinit var admin: String
    lateinit var name: String
    lateinit var user: List<String>
    lateinit var id: String
    lateinit var imageUrl: String
    lateinit var lowercasename: String
}

class PlayGroup {
    lateinit var admin: String
    lateinit var name: String
    lateinit var user: List<String>
    lateinit var id: String
    lateinit var imageUrl: String
    lateinit var lowercasename: String

}

class Scene {
    lateinit var thumbnail: String
    lateinit var name: String
    lateinit var id: String
}

class Card {
    lateinit var imageUrl: String
    lateinit var type: String
    lateinit var title: String
}