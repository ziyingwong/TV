package com.example.superWiserTVV2

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.ContactsContract
import android.support.v17.leanback.widget.ImageCardView
import android.support.v17.leanback.widget.Presenter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.*
import com.bumptech.glide.Glide

class ControlFragment : Fragment() {

    lateinit var play: ImageView
    lateinit var previous: ImageView
    lateinit var next: ImageView
    lateinit var time: ImageView
    lateinit var sceneName: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.diy_control, container, false)
        var myFragmentManager = activity!!.supportFragmentManager

        sceneName = v.findViewById(R.id.sceneName)
        play = v.findViewById(R.id.playpause)
        previous = v.findViewById(R.id.previous)
        next = v.findViewById(R.id.next)
        time = v.findViewById(R.id.timer)
        if (!DataContainer.isRunning) {
            play.setImageResource(R.drawable.ic_play_arrow_white_48dp)
        }

        play.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p0!!.isFocused) {
                    play.setBackgroundResource(R.drawable.round_button)
                } else {
                    play.background = null
                }
            }

        }
        play.setOnClickListener {
            Log.e("mytag", "clicked")
            if (DataContainer.isRunning) {
                Log.e("mytag", "pause now")
                DataContainer.isRunning = false
                play.setImageResource(R.drawable.ic_play_arrow_white_48dp)
            } else {
                Log.e("mytag", "play now")
                DataContainer.isRunning = true
                DataContainer.initTimer(myFragmentManager, DataContainer.counter.toLong())
                play.setImageResource(R.drawable.ic_pause_black_24dp)
            }
        }
        previous.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p0!!.isFocused) {
                    previous.setBackgroundResource(R.drawable.round_button)
                } else {
                    previous.background = null
                }
            }

        }
        previous.setOnClickListener {
            DataContainer.isRunning = false
            Handler().postDelayed({
                DataContainer.selected -= 1
                lateinit var fragmentTransaction: FragmentTransaction
                fragmentTransaction = myFragmentManager.beginTransaction()
                if (myFragmentManager.backStackEntryCount > 0) {
                    myFragmentManager.popBackStack()
                }
                var fragment = PlayGroupScene()
                fragmentTransaction.replace(R.id.playback_fragment, fragment)
                fragmentTransaction.commit()
            }, 1000)

        }
        next.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p0!!.isFocused) {
                    next.setBackgroundResource(R.drawable.round_button)
                } else {
                    next.background = null
                }
            }

        }
        next.setOnClickListener {
            Log.e("mytag", "next")
            DataContainer.isRunning = false
            Handler().postDelayed({
                DataContainer.selected += 1
                lateinit var fragmentTransaction: FragmentTransaction
                fragmentTransaction = myFragmentManager.beginTransaction()
                if (myFragmentManager.backStackEntryCount > 0) {
                    myFragmentManager.popBackStack()
                }
                var fragment = PlayGroupScene()
                fragmentTransaction.replace(R.id.playback_fragment, fragment)
                fragmentTransaction.commit()
            }, 1000)
        }
        time.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p0!!.isFocused) {
                    time.setBackgroundResource(R.drawable.round_button)
                } else {
                    time.background = null
                }
            }
        }
        time.setOnClickListener {
            Log.e("mytag", "set time")
            DataContainer.isRunning = false

            Handler().postDelayed({
                var builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.myDialog))
                var mView = layoutInflater.inflate(R.layout.settime, null)
                builder.setTitle("Set time interval")
                var minPicker = mView.findViewById<NumberPicker>(R.id.minutes)
                minPicker.isFocusable = true
                minPicker.setOnFocusChangeListener { view, b ->
                    if (view!!.isFocused) {
                        minPicker.setBackgroundResource(R.drawable.picker_focus)
                    } else {
                        minPicker.background = null
                    }
                }
                var sPicker = mView.findViewById<NumberPicker>(R.id.seconds)
                sPicker.isFocusable = true
                sPicker.setOnFocusChangeListener { view, b ->
                    if (view!!.isFocused) {
                        sPicker.setBackgroundResource(R.drawable.picker_focus)
                    } else {
                        sPicker.background = null
                    }
                }
                var mValue = arrayOf("0", "1", "2", "3", "4")
                var sValueShort = arrayOf("30", "35", "40", "45", "50", "55")
                var sValueLong =
                    arrayOf("0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55")
                minPicker.maxValue = 4
                minPicker.minValue = 0
                sPicker.maxValue = 5
                sPicker.minValue = 0
                minPicker.displayedValues = arrayOf("0", "1", "2", "3", "4")
                sPicker.displayedValues = sValueShort
                minPicker.setOnValueChangedListener(NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
                    if (newVal == 0) {
                        if (sPicker.value < 6) {
                            sPicker.value = 0
                        } else {
                            sPicker.value = sPicker.value - 6
                        }
                        sPicker.maxValue = 5
                        sPicker.displayedValues = sValueShort

                    } else if (oldVal == 0) {
                        sPicker.displayedValues = sValueLong
                        sPicker.maxValue = 11
                        sPicker.value = sPicker.value + 6
                    }
                })

                mView.minimumHeight = 600
                builder.setView(mView)

                builder.setPositiveButton("Save") { dialog, which ->
                    Log.e("myTag", "time interval saved")
                    var second = sPicker.value
                    if (minPicker.value == 0) {
                        second = sValueShort[second].toInt()
                    } else {
                        second = sValueLong[second].toInt()
                    }
                    var newTime = mValue[minPicker.value].toInt() * 60 + second
                    DataContainer.timeInterval = newTime * 1000

                    DataContainer.isRunning = true
                    DataContainer.counter = DataContainer.timeInterval
                    DataContainer.initTimer(myFragmentManager, DataContainer.timeInterval.toLong())
                    dialog.dismiss()

                }

                builder.setNegativeButton("Cancel") { dialog, which ->
                    Log.e("myTag", "cancelled")

                    DataContainer.isRunning = true
                    DataContainer.initTimer(myFragmentManager, DataContainer.counter.toLong())
                    dialog.dismiss()

                }
                minPicker.requestFocus()
                builder.show()
            }, 1000)

        }

        return v
    }

    override fun onStart() {
        super.onStart()
        play.requestFocus()
    }


}