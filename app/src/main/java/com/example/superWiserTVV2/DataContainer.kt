package com.example.superWiserTVV2

import android.os.CountDownTimer
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log

object DataContainer {

    var playscene = ArrayList<Scene>()
    var timeInterval = 30000
    var selected = 0
    var isRunning = false
    var counter = 30000


    fun initTimer(myFragmentManager: FragmentManager, duration: Long) {
        Log.e("mytag", "inited ticking : ${selected} counter ${counter} duration ${duration}")

        object : CountDownTimer(duration, 1000) {
            override fun onFinish() {
                Log.e("mytag", "$selected : finished")
                isRunning = false
                selected += 1
                lateinit var fragmentTransaction: FragmentTransaction
                fragmentTransaction = myFragmentManager.beginTransaction()
                if (myFragmentManager.backStackEntryCount > 0) {
                    myFragmentManager.popBackStack()
                }
                var fragment = PlayGroupScene()
                fragmentTransaction.replace(R.id.playback_fragment, fragment)
                fragmentTransaction.commit()
            }

            override fun onTick(p0: Long) {
                if (isRunning) {
                    counter = p0.toInt()
                } else {
                    Log.e("mytag", "$selected : paused/stoped")
                    cancel()
                }
                Log.e("mytag", "ticking init ${selected}: ${counter}")
            }
        }.start()
    }

}

