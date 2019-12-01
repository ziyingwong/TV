package com.example.superWiserTVV2

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView


class PlaySceneActivity : FragmentActivity() {
    val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playgroup_fragment)
        lateinit var fragmentTransaction: FragmentTransaction
        fragmentTransaction = fragmentManager.beginTransaction()
        var fragment = PlayGroupScene()
        fragmentTransaction.replace(R.id.playback_fragment, fragment)
        fragmentTransaction.commit()

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        //create control fragment
        if (fragmentManager.backStackEntryCount == 0 && keyCode != KeyEvent.KEYCODE_BACK) {
            lateinit var fragmentTransaction: FragmentTransaction
            fragmentTransaction = fragmentManager.beginTransaction()
            var controlFragment = ControlFragment()
            fragmentTransaction.add(R.id.playback_fragment, controlFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        //add row of coming up scene
        if (fragmentManager.fragments.get(fragmentManager.fragments.lastIndex) is ControlFragment && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            var fragment = fragmentManager.fragments.get(fragmentManager.fragments.lastIndex) as ControlFragment

            if (fragment.view!!.findViewById<LinearLayout>(R.id.showNext).childCount <= (DataContainer.playscene.size - 1) / 4) {
                loadRow(fragment)
            }
            Log.e("mytag", "down")
        }

        return super.onKeyDown(keyCode, event)
    }

    fun loadRow(fragment: ControlFragment) {
        //original linear layout
        var linearLayout = fragment.view!!.findViewById<LinearLayout>(R.id.showNext)
        var child = linearLayout.childCount

        //new linear layout to add to original layout
        var layout = LinearLayout(fragment.context)
        layout.orientation = LinearLayout.HORIZONTAL

        //new linear layout to contain card
        var cardViewLayout = LinearLayout(fragment.context)

        //layout param for each card
        val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.weight = 1.toFloat()

        //layout param for new linear layout
        val lp2 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        for (i in 0..3) {

            var previewCardView = layoutInflater.inflate(R.layout.preview_card, cardViewLayout, false)

            var positionToAdd = child * 4 + DataContainer.selected + i
            if (positionToAdd >= DataContainer.playscene.size) {
                positionToAdd %= DataContainer.playscene.size
                if (positionToAdd >= DataContainer.selected) {
                    var space = Space(fragment.context)
                    space.setPadding(10, 10, 10, 10)
                    layout.addView(space, lp)
                    continue
                }
            }
            var item = DataContainer.playscene.get(positionToAdd)
            var encodedString = item.thumbnail.trim()
            encodedString = encodedString.replace("data:image/png;base64,", "")
            var decodedString = Base64.decode(encodedString, Base64.DEFAULT) as ByteArray
            var bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            previewCardView.findViewById<ImageView>(R.id.previewImage).setImageBitmap(bitmap)
            previewCardView.findViewById<TextView>(R.id.previewTitle).text = item.name
            previewCardView.setPadding(10, 10, 10, 10)
            previewCardView.isFocusable = true
            previewCardView.isClickable = true
            previewCardView.onFocusChangeListener = object : View.OnFocusChangeListener {
                override fun onFocusChange(p0: View?, p1: Boolean) {
                    if (p0!!.isFocused) {
                        p0.setBackgroundColor(Color.parseColor("#90FFFFFF"))
                    } else {
                        p0.background = null
                    }
                }
            }
            previewCardView.setOnClickListener {
                DataContainer.isRunning = false
                Handler().postDelayed({
                    DataContainer.selected = DataContainer.playscene.indexOf(item)
                    Log.e("mytag", item.name)
                    lateinit var fragmentTransaction: FragmentTransaction
                    fragmentTransaction = fragmentManager.beginTransaction()
                    if (fragmentManager.backStackEntryCount > 0) {
                        fragmentManager.popBackStack()
                    }
                    var fragment = PlayGroupScene()
                    fragmentTransaction.replace(R.id.playback_fragment, fragment)
                    fragmentTransaction.commit()
                }, 1000)
            }
            layout.addView(previewCardView, lp)
        }

        linearLayout.addView(layout, lp2)
    }


    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
            DataContainer.isRunning = false
            DataContainer.counter = DataContainer.timeInterval
            DataContainer.playscene.clear()
            DataContainer.selected = 0

        }
    }
}