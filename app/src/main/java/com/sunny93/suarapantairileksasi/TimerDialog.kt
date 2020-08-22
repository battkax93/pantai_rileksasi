package com.sunny93.suarapantairileksasi

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window
import kotlinx.android.synthetic.main.dialog_timer.*


@Suppress("DEPRECATION")
class TimerDialog(context: Context) : Dialog(context) {

    private var timeData: Int = 0
    private var prefTimeData: String = "TIME DATA"
    private lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_timer)
        this.window
            ?.attributes!!.windowAnimations = R.style.DialogAnimation
        init()
    }

    private fun init() {
        val intent = Intent()
        preference = context.getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
        val editor = preference.edit()

        spinnerTimer.apply {
            setOnSpinnerItemSelectedListener<String> { position, item ->
                spinnerTimer.hint = item
                timeData = position
                when (timeData) {
                    0 -> {
                        editor.putLong(prefTimeData, 60_000L)
                        editor.apply()
                    }
                    1 -> {
                        editor.putLong(prefTimeData, 180_000L)
                        editor.apply()
                    }
                    2 -> {
                        editor.putLong(prefTimeData, 360000)
                        editor.apply()
                    }
                    3 -> {
                        editor.putLong(prefTimeData, 600_000L)
                        editor.apply()
                    }
                    4 -> {
                        editor.putLong(prefTimeData, 900_000L)
                        editor.apply()
                    }
                }

                timeData = preference.getLong(prefTimeData, 0).toInt()
                println("saved $timeData")
                bSave.visibility = View.VISIBLE
            }
        }
        bSave.setOnClickListener { dismiss() }
    }
}